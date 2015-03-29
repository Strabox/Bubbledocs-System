package pt.tecnico.bubbledocs;

import pt.ist.fenixframework.Atomic;


import pt.tecnico.bubbledocs.domain.*;
import pt.tecnico.bubbledocs.service.CreateSpreadSheet;
import pt.tecnico.bubbledocs.service.CreateUser;
import pt.tecnico.bubbledocs.service.LoginUser;


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
			LoginUser rootLogin =  new LoginUser("root", "root");
			rootLogin.execute();
			String rootToken =  rootLogin.getUserToken();
			new CreateUser(rootToken, "pf", "sub", "Paul Door").execute();
			new CreateUser(rootToken,"ra","cor","Step Rabbit").execute();
			LoginUser pfLogin = new LoginUser("pf", "sub");
			pfLogin.execute();
			String pfToken = pfLogin.getUserToken();
			new CreateSpreadSheet(pfToken,"NotasEs",300,20);
		}
		catch(Exception e){
			System.out.println(e);
		}
	}

}
