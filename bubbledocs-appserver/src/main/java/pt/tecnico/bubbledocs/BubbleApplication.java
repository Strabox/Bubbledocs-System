package pt.tecnico.bubbledocs;

import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.TransactionManager;

import javax.transaction.*;

import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import pt.tecnico.bubbledocs.domain.*;


public class BubbleApplication {
	
	/* setupIfNeed - Setup the initial state if BubbleDocs is empty. */
    private static void setupIfNeed(Bubbledocs b) {
		if (b.getFolhaCalculoSet().isEmpty() && b.getUtilizadorSet().isEmpty())
		    SetupBubbledocs.populateDomain();
    }
	
    /* main - Bubbledocs main function. */
	public static void main(String[] args){
		System.out.println("Bem-Vindos ao BubbleDocs!!!");
		TransactionManager tm = FenixFramework.getTransactionManager();
    	boolean committed = false;				
		
		try{
			/*======== Espaço que contém as chamadas para visualizacao da 1ºEntrega ======*/
			tm.begin();
			Bubbledocs bubble = Bubbledocs.getInstance();
			/* Se for a 1º vez a correr ele vai preencher a BD com o necessário.*/
			setupIfNeed(bubble);
			
			bubble.listAllUsers();
			User pf = bubble.getUserByName("pf");
			User ra = bubble.getUserByName("ra");
			
			pf.listAllSpreadSheets();
			ra.listAllSpreadSheets();

			XMLOutputter xml = new XMLOutputter();
	    	xml.setFormat(Format.getPrettyFormat());
	    	org.jdom2.Document xmlout = pf.getSpreadSheet("Notas Es").get(0).exportToXML();
	    	System.out.println(xml.outputString(xmlout));
			
			pf.getSpreadSheet("Notas Es").get(0).delete();
			
			//FIX-ME Import SpreadSheet
			
			pf.listAllSpreadSheets();
			
			tm.commit();
			committed = true;
			/*============================================================================*/
		}
		catch(Exception e){
			System.out.println(e);
		} finally {
			if(!committed){
				try{
					tm.rollback();
				}
				catch(Exception e){
					System.err.println(e);
				}
			}
		}

	}

}
