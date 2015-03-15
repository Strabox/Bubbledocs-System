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
	 * Use only to demonstrate 1st part, needed due to fenixframework
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
			/* If it's 1st execution DB will be loaded with some data */
			setupIfNeed(bubble);
			
			bubble.listAllUsers();
			
			User pf = bubble.getUserByName("pf");
			User ra = bubble.getUserByName("ra");
			System.out.println("================================");
			
			System.out.println("======= pf's Spreadsheets ======");
			pf.listOwnedSpreadSheets();
			System.out.println("================================");
			
			System.out.println("======= ra's Spreadsheets ======");
			ra.listOwnedSpreadSheets();
			System.out.println("================================");
			
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
			System.out.println("================================");
			
	    	xml.setFormat(Format.getPrettyFormat());
	    	xmlout = pf.getSpreadSheet("Notas Es").get(0).exportToXML();
	    	System.out.println(xml.outputString(xmlout));
			
		}
		catch(Exception e){
			System.out.println(e);
		}
	}

}
