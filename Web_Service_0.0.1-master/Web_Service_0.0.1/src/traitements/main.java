/*
 * 
 * 
 * 
 * 
 * NON UTILISE
 * (MODE DEBUG)
 * 
 * 
 * 
 * 
 * 
 */

package traitements;

import java.io.File;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

public class main {
	
	static Document document;
	static Element racine;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		System.out.println(System.getProperty("user.dir"));
		
		String filepath = System.getProperty("user.dir");
		
		SAXBuilder sxb = new SAXBuilder();
		try
		{
			File file = new File(filepath+"/services-a-domicile.xml");
			document = sxb.build(file);
		}
		catch(Exception e){
			System.out.println("Fichier non chargé");
			System.out.println(e);
		}

	}

}
