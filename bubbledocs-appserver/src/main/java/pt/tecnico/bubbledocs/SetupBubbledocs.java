package pt.tecnico.bubbledocs;

import pt.ist.fenixframework.Atomic;
import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.domain.Utilizador;

public class SetupBubbledocs {
	
	@Atomic
	public static void main(String[] args){
		populate();
	}
	
	public static void populate(){
		Bubbledocs.getInstance();
		new Utilizador("SuperUser","root","root");
	}
}
