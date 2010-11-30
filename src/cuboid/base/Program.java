package cuboid.base;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cuboid.base.visualisation.VisualisationWindow;
import cuboid.solvers.AproximationAlgorithm;
import cuboid.solvers.CloningAlgorithm;
import cuboid.solvers.ExactSolutionFinder;
import cuboid.solvers.SolutionFinder;

public class Program {
	static List<BlockCollection> blockCollections;

	static void showValidUsage(){
		System.out.println("Valid usage:\n"+
			"java -jar kostka2010.jar input output viz nr [param1] [param2]\n\n"+
			"where:\n"+
			" input - block set file\n"+
			" output - solution file\n"+
			" viz - visualization (on when viz = 1)\n"+
			" nr - algorithm number (1 - exact, 2 - aproximation, 3 - cloning)\n"+
			" [param1] - parameter only for:\n"+
			"    aproxmiation (param1 >= 1)"+
			"    cloning (param1>=1)\n"+
			" [param2] - parameter only for:\n"+
			"    cloning (0<param2<1)\n");
	}

	static void readFile(String filename){
		File file = new File(filename);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try{
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();
			Node root = doc.getDocumentElement();
//			System.out.println("root = "+root);
//			System.out.println("root.children = "+root.getChildNodes().getLength());
			if(!"blocksProject".equals(root.getNodeName()))
				throw new BlockSetFormatException("Root node must be blocksProject.");
//			 	|| root.getChildNodes().getLength()!=1
//					|| !"blocksSet".equals(root.getFirstChild().getNodeName()))
			NodeList children = root.getChildNodes();
			int n = children.getLength();
			boolean one_node = false;
			Node setNode = null;
			for(int i=0;i<n;++i){
				Node node = children.item(i);
				if(node.getNodeType()==Node.ELEMENT_NODE)
					if(one_node || !"blockSet".equals(node.getNodeName()))
						throw new BlockSetFormatException("Only one tag inside root and must be blockSet.");
					else{
						setNode = node;
						one_node = true;
					}
			}
			if(!one_node) throw new BlockSetFormatException("No blockSet inside root.");

			children = setNode.getChildNodes();
			n = children.getLength();
			blockCollections = new ArrayList<BlockCollection>();
			for(int i=0;i<n;++i){
				Node node = children.item(i);
				if(node.getNodeType()==Node.ELEMENT_NODE)
					if(!"block".equals(node.getNodeName()) || node.getAttributes().getLength()!=2
							|| !node.getAttributes().item(0).getNodeName().equals("amount")
							|| !node.getAttributes().item(1).getNodeName().equals("structure")){
						System.out.println("attr. = "+node.getAttributes().item(0).getNodeName());
						throw new BlockSetFormatException("First attribute of block must be amount and second one must be structure.");
					}else{
						int amount = Integer.parseInt(node.getAttributes().item(0).getNodeValue());
						if(amount<1)
							throw new BlockSetFormatException("amount must be greater than 0.");
						String structure = node.getAttributes().item(1).getNodeValue();
						BlockCollection bc = new BlockCollection(new Block(structure),amount);
						blockCollections.add(bc);
						//System.out.println("nowy blok = "+bc);
					}
			}
			
		}catch(Exception e){
			e.printStackTrace();
			showValidUsage();
		}
	}

	static void saveToFile(Solution solution, String filename){
		BufferedWriter writer = null;
		try{
			writer = new BufferedWriter(new FileWriter(filename));
			writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			writer.write("\n<blocksProject>");
			writer.write("\n\t<blockSet>");
			int i = 0;
			for(BlockCollection bc:blockCollections){
				writer.write("\n\t\t<block id=\""+(i++)+"\" amount=\""+bc.getAmount()+"\" structure=\""+bc.getBlock().getStructure()+"\"/>");
				writer.flush(); // przy dużych danych, lepiej kawałkami zapisywać
			}
			writer.write("\n\t</blockSet>");
			writer.write("\n\t<solution>");
			if(solution != null){
				List<Move> moves = solution.getSequence();
				for(Move move:moves)
					writer.write("\n\t\t<block id=\""+move.getId()+"\" x=\""+move.getX()+"\" y=\""+move.getY()+"\" z=\""+move.getZ()+"\""+(move.getOrId()>0?" rotationMask=\""+move.getOrId()+"\"":"")+"/>");
			}
			writer.write("\n\t</solution>");
			writer.write("\n</blocksProject>");
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(writer!=null){
					writer.flush();
					writer.close();
				}
			}catch(IOException ioe){
				ioe.printStackTrace();
			}
		}
	}

	public static void main(String[] args){
		if((args.length>6 || args.length<4) ||
			(args[3].equals("1") && args.length!=4) ||
			(args[3].equals("2") && args.length>5))
		{
			System.err.println("Wrong usage!\n");
			showValidUsage();
			return;
		}

		readFile(args[0]);
		System.out.println("Read: OK.");

		List<SolutionFinder> algorithm=new LinkedList<SolutionFinder>();

		int param=20;

		if(args.length==5) 
			param=Integer.parseInt(args[4]);
		if(param<0) param=-param;

		int param1=5;
		double param2=0.9;

		if(args.length==5)
			param1=Integer.parseInt(args[4]);
		if(param1<0) param1=-param1;
		if(args.length==6){
			param1=Integer.parseInt(args[4]);
			param2=Double.parseDouble(args[5]);
		}

		switch(Integer.parseInt(args[3])){
			case 1:
				algorithm.add(new ExactSolutionFinder());
				break;
			case 2:
				algorithm.add(new AproximationAlgorithm(param));
				break;
			case 3:
				algorithm.add(new CloningAlgorithm(param1,param2));
				break;
			case 4:
				algorithm.add(new ExactSolutionFinder());
				algorithm.add(new AproximationAlgorithm(10));
				algorithm.add(new CloningAlgorithm(10, 0.9));
				break;
			default:
				showValidUsage();
				return;
		}

		Iterator<SolutionFinder> it=algorithm.iterator();
		while(it.hasNext())
		{
			SolutionFinder s=it.next();
			System.out.println();
			System.out.println(s);
			System.out.println("---------------------------");
			for(BlockCollection bc:blockCollections)
				System.out.println(bc);
			System.out.println("---------------------------");
			long start=System.currentTimeMillis();
			Solution solution = s.solve(blockCollections);
			long end=System.currentTimeMillis();
			System.out.println("TIME: "+(end-start)+"ms");
			if(solution!=null)
			{
				System.out.println("SOLUTION VOLUME: "+solution.getVolume());
				if(args[2].equals("1"))
					(new VisualisationWindow(solution)).setVisible(true);

				System.out.println("solution = "+solution);
			}
			else
				System.out.println("COULDN'T FIND SOLUTION");
			
			if(algorithm.size()==1)
				saveToFile(solution,args[1]);
		}
	}

}
