package pt.tecnico.bubbledocs;


import pt.tecnico.bubbledocs.service.AssignLiteralCell;
import pt.tecnico.bubbledocs.service.AssignReferenceCell;
import pt.tecnico.bubbledocs.service.CreateSpreadSheet;
import pt.tecnico.bubbledocs.service.CreateUser;
import pt.tecnico.bubbledocs.service.LoginUser;


/*
 * Class SetupBubbledocs - Used to fill application database
 * in the 1st time it's executed.
 */
public class SetupBubbledocs {
	

	public static void main(String[] args){
		populateDomain();
	}
	
	public static void populateDomain(){
		try{
			System.out.println("----------First Time Populate Initializing!!-------");
			LoginUser rootLogin =  new LoginUser("root", "root");
			rootLogin.execute();
			String rootToken =  rootLogin.getUserToken();
			new CreateUser(rootToken, "pf", "sub", "Paul Door").execute();
			new CreateUser(rootToken,"ra","cor","Step Rabbit").execute();
			LoginUser pfLogin = new LoginUser("pf", "sub");
			pfLogin.execute();
			String pfToken = pfLogin.getUserToken();
			CreateSpreadSheet cs = new CreateSpreadSheet(pfToken,"NotasEs",300,20);
			cs.execute();
			new AssignLiteralCell(pfToken, cs.getSheetId(), "3;4", "5").execute();
			//new AssignReferenceCell(pfToken, cs.getSheetId(), "1;1", "5;6").execute();
			
			System.out.println("----------First Time Populate Done!!-------");
		}
		catch(Exception e){
			System.out.println(e);
		}
	}	

	
}
