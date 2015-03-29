package pt.tecnico.bubbledocs;

import javax.transaction.SystemException;

import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.TransactionManager;
import pt.tecnico.bubbledocs.domain.*;
import pt.tecnico.bubbledocs.service.ExportDocument;
import pt.tecnico.bubbledocs.service.LoginUser;



public class BubbleApplication {
	
	/* setupIfNeed - Setup the initial state if BubbleDocs is empty. */
    private static void setupIfNeed(Bubbledocs b) {
		if (b.getBubbleSpreadsheetSet().isEmpty() && b.getUserSet().size() == 1)
		    SetupBubbledocs.populateDomain();
    }
	
    /* main - Bubbledocs main function. */
	public static void main(String[] args){
		System.out.println("Bem-Vindos ao BubbleDocs!!!");				
		boolean committed = false;
		TransactionManager	tm	=FenixFramework.getTransactionManager();
		try{
			tm.begin();
			Bubbledocs bubble = Bubbledocs.getInstance();
			setupIfNeed(bubble);
			
			bubble.listAllUsers();
			System.out.println("----------------------------");
			bubble.listAllSpreadSheets();
			System.out.println("----------------------------");
			LoginUser pfLogin = new LoginUser("pf", "sub");
			pfLogin.execute();
			String pfToken = pfLogin.getUserToken();
			
			ExportDocument ex = new ExportDocument(pfToken, 0);
			ex.execute();
			
			XMLOutputter xml = new XMLOutputter();
	    	xml.setFormat(Format.getPrettyFormat());
	    	org.jdom2.Document xmlout = ex.getDocXML();
	    	System.out.println(xml.outputString(xmlout));
	    	System.out.println("----------------------------");
	    	
	    	bubble.getSpreadSheet(0).delete();
	    	System.out.println("----------------------------");
	    	bubble.listAllSpreadSheets();
	    	System.out.println("----------------------------");
	    	
	    	//SpreadSheet s = new SpreadSheet();
	    	//s.importFromXML(xmlout, "pf);
	    	//pf.addOwned(s);
	    	
	    	bubble.listAllSpreadSheets();
	    	System.out.println("----------------------------");
	    	
			tm.commit();
			committed = true;
		}
		catch(Exception e){
			System.out.println(e);
		}finally{
			if(!committed){
				try{
					tm.rollback();
				}
				catch(SystemException e){
					System.err.println(e);
				}
			}
		}
	}

}
