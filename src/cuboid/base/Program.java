package cuboid.base;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class Program {

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
			System.out.println("root = "+root);
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
