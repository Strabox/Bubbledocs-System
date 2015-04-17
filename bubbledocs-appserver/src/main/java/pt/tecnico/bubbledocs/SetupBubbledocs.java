package pt.tecnico.bubbledocs;

/*
import pt.tecnico.bubbledocs.domain.ADD;
import pt.tecnico.bubbledocs.domain.DIV;
import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.domain.Literal;
import pt.tecnico.bubbledocs.domain.Reference;
import pt.tecnico.bubbledocs.exceptions.NoValueForReferenceException;
import pt.tecnico.bubbledocs.service.AssignLiteralCell;
import pt.tecnico.bubbledocs.service.AssignReferenceCell;
import pt.tecnico.bubbledocs.service.CreateSpreadSheet;
import pt.tecnico.bubbledocs.service.CreateUser;
import pt.tecnico.bubbledocs.service.LoginUser;
*/

/*
 * Class SetupBubbledocs - Used to fill application database
 * in the 1st time it's executed.
 */
public class SetupBubbledocs {
	

	public static void main(String[] args){
		populateDomain();
	}
	
	public static void populateDomain(){
		/*
		try{
			Bubbledocs bubble = Bubbledocs.getInstance();
			System.out.println("----------First Time Populate Initializing!!-------");
			LoginUser rootLogin =  new LoginUser("root", "root");
			rootLogin.execute();
			String rootToken =  rootLogin.getUserToken();
			new CreateUser(rootToken, "pfa", "pfa@ist.pt", "Paul Door").execute();
			new CreateUser(rootToken,"raa","raa@ist.pt","Step Rabbit").execute();
			LoginUser pfLogin = new LoginUser("pf", "sub");
			pfLogin.execute();
			String pfToken = pfLogin.getUserToken();
			CreateSpreadSheet cs = new CreateSpreadSheet(pfToken,"NotasEs",300,20);
			cs.execute();
			new AssignLiteralCell(pfToken, cs.getSheetId(), "3;4", "5").execute();
			try{
				new AssignReferenceCell(pfToken, cs.getSheetId(), "1;1", "5;6").execute();
			}
			catch(NoValueForReferenceException e){}
			
			bubble.getSpreadSheet(0).addContentToCell(5, 6, new ADD(new Literal(2),new Reference(3,4)));
			bubble.getSpreadSheet(0).getSingleCell(5,6).getContent().mountReference(bubble.getSpreadSheet(0).getSingleCell(5,6));
			bubble.getSpreadSheet(0).addContentToCell(2, 2, new DIV(new Reference(1,1),new Reference(3,4)));
			bubble.getSpreadSheet(0).getSingleCell(2,2).getContent().mountReference(bubble.getSpreadSheet(0).getSingleCell(2,2));
			System.out.println("----------First Time Populate End!!-------");
		}
		catch(Exception e){
			System.out.println(e);
		}
		*/
	}	

	
}


