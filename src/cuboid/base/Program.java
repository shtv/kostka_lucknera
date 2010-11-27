package cuboid.base;

import java.io.File;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Program {
	static List<BlockCollection> blockCollections;

	static void showValidUsage(){
		System.out.println("Valid usage:\njava -jar kostka2010.jar input output\n\nwhere:\n input - block set file\n output - solution file");
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
						String structure = node.getAttributes().item(1).getNodeValue();
						BlockCollection bc = new BlockCollection(new Block(structure),amount);
						blockCollections.add(bc);
//						System.out.println("nowy blok");
					}
			}
			
		}catch(Exception e){
			e.printStackTrace();
			showValidUsage();
		}
	}

	public static void main(String[] args){
		if(args.length!=2){
			System.err.println("Wrong usage!\n");
			showValidUsage();
			return;
		}
		readFile(args[0]);
		System.out.println("hello!");
	}

}
