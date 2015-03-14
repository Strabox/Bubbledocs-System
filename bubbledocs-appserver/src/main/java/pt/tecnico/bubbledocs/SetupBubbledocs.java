package pt.tecnico.bubbledocs;


import pt.ist.fenixframework.Atomic;
import pt.tecnico.bubbledocs.domain.ADD;
import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.domain.NumberInt;
import pt.tecnico.bubbledocs.domain.Reference;
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
		Bubbledocs bubble = Bubbledocs.getInstance();
		bubble.addUtilizador(new User("Paul Door","pf","sub"));
		bubble.addUtilizador(new User("Step Rabbit","ra","cor"));
		User pf = bubble.getUserByName("pf");
		pf.addOwned(new SpreadSheet("Notas Es",300,20));
		SpreadSheet pfSpreadSheet = pf.getSpreadSheet("Notas Es").get(0);
		pfSpreadSheet.addContentToCell(3, 4, new NumberInt(5));
		//pfSpreadSheet.addContentToCell(1, 1, new Reference(5,6));
		//pfSpreadSheet.addContentToCell(5, 6, new ADD(new NumberInt(2),new Reference(3,4)));
		//pfSpreadSheet.addContentToCell(2, 2, new DIV(new Reference(1,1),new Reference(3,4)));
	}

	
}
