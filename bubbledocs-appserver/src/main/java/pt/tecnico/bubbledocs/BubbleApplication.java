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
	
	/*
	 * Use only to demonstrate 1st part, need due to fenixframework
	 * atomic acesses.
	 */
	@Atomic
	public static SpreadSheet createEmptySpreadSheet(){
		return new SpreadSheet();
	}
	
    /* main - Bubbledocs main function. */
	public static void main(String[] args){
		System.out.println("Bem-Vindos ao BubbleDocs!!!");				
		
		try{

			Bubbledocs bubble = Bubbledocs.getInstance();
			/* Se for a 1º vez a correr ele vai preencher a BD com o necessário.*/
			setupIfNeed(bubble);
			
			bubble.listAllUsers();
			
			System.out.println("======== USERS END =========");
			User pf = bubble.getUserByName("pf");
			User ra = bubble.getUserByName("ra");
			
			System.out.println("======= pf's Spreadsheets ======");
			pf.listOwnedSpreadSheets();

			System.out.println("======= ra's Spreadsheets ======");
			ra.listOwnedSpreadSheets();

			XMLOutputter xml = new XMLOutputter();
	    	xml.setFormat(Format.getPrettyFormat());
	    	org.jdom2.Document xmlout = pf.getSpreadSheet("Notas Es").get(0).exportToXML();
	    	System.out.println(xml.outputString(xmlout));
			
			pf.getSpreadSheet("Notas Es").get(0).delete();
			
	    	SpreadSheet s2 = createEmptySpreadSheet();
	    	s2.importFromXML(xmlout,"pf");
	    	pf.addOwned(s2);
	    	
	    	System.out.println("======= pf's Spreadsheets======");
			pf.listOwnedSpreadSheets();

			
	    	xml.setFormat(Format.getPrettyFormat());
	    	xmlout = pf.getSpreadSheet("Notas Es").get(0).exportToXML();
	    	System.out.println(xml.outputString(xmlout));
			
		}
		catch(Exception e){
			System.out.println(e);
		}
	}

}
