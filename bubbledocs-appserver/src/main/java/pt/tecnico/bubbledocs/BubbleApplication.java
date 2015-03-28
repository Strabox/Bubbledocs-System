package pt.tecnico.bubbledocs;

import pt.ist.fenixframework.Atomic;


import pt.tecnico.bubbledocs.domain.*;


public class BubbleApplication {
	
	/* setupIfNeed - Setup the initial state if BubbleDocs is empty. */
	@Atomic
    private static void setupIfNeed(Bubbledocs b) {
		if (b.getBubbleSpreadsheetSet().isEmpty() && b.getUserSet().isEmpty())
		    SetupBubbledocs.populateDomain();
    }
	
	
    /* main - Bubbledocs main function. */
	public static void main(String[] args){
		System.out.println("Bem-Vindos ao BubbleDocs!!!");				
		
		try{
				
		}
		catch(Exception e){
			System.out.println(e);
		}
	}

}
