package pt.tecnico.bubbledocs;

import pt.ist.fenixframework.Atomic;

import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;


import pt.tecnico.bubbledocs.domain.*;


public class BubbleApplication {
	
	/* setupIfNeed - Setup the initial state if BubbleDocs is empty. */
	@Atomic
    private static void setupIfNeed(Bubbledocs b) {
		if (b.getFolhaCalculoSet().isEmpty() && b.getUtilizadorSet().isEmpty())
		    SetupBubbledocs.populateDomain();
    }
	
    /* main - Bubbledocs main function. */
	public static void main(String[] args){
		System.out.println("Bem-Vindos ao BubbleDocs!!!");				
		
		try{

			Bubbledocs bubble = Bubbledocs.getInstance();
			/* Se for a 1º vez a correr ele vai preencher a BD com o necessário.*/
			setupIfNeed(bubble);
			
			bubble.listAllUsers();
			User pf = bubble.getUserByName("pf");
			User ra = bubble.getUserByName("ra");
			
			for(SpreadSheet s: pf.listOwnedSpreadSheets()){
				System.out.println(s);
			}
			for(SpreadSheet s: ra.listOwnedSpreadSheets()){
				System.out.println(s);
			}

			XMLOutputter xml = new XMLOutputter();
	    	xml.setFormat(Format.getPrettyFormat());
	    	org.jdom2.Document xmlout = pf.getSpreadSheet("Notas Es").get(0).exportToXML();
	    	System.out.println(xml.outputString(xmlout));
			
			//pf.getSpreadSheet("Notas Es").get(0).delete();
			
			pf.listOwnedSpreadSheets();
			
			//FIX-ME Import SpreadSheet
			
			
		}
		catch(Exception e){
			System.out.println(e);
		}
	}

}
