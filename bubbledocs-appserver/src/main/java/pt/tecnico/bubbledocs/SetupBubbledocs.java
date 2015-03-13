package pt.tecnico.bubbledocs;

import pt.ist.fenixframework.Atomic;
import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.domain.SpreadSheet;
import pt.tecnico.bubbledocs.domain.User;
import pt.tecnico.bubbledocs.exceptions.DuplicateUsernameException;

/*
 * Class SetupBubbledocs - Usada para preencher a Base de Dados
 * com o necessário se esta for a 1º execucação da Aplicação.
 */
public class SetupBubbledocs {
	
	@Atomic
	public static void main(String[] args){
		populateDomain();
	}
	
	public static void populateDomain(){
		try{
			Bubbledocs bubble = Bubbledocs.getInstance();
			bubble.addUtilizador(new User("Paul Door","pf","sub"));
			bubble.addUtilizador(new User("Step Rabbit","ra","cor"));
			User pf = bubble.getUserByName("pf");
			pf.addOwned(new SpreadSheet("Notas Es",300,20));
			SpreadSheet pfSpreadSheet = pf.getSpreadSheet("Notas Es").get(0);
			//FIX-ME add content to SpreadSheet
		}
		catch(DuplicateUsernameException e){
			System.out.println(e);
		}
	}
	
}
