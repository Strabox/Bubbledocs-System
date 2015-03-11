package pt.tecnico.bubbledocs;

import pt.ist.fenixframework.Atomic;
import pt.tecnico.bubbledocs.domain.Bubbledocs;
import pt.tecnico.bubbledocs.domain.FolhaCalculo;
import pt.tecnico.bubbledocs.domain.Literal;
import pt.tecnico.bubbledocs.domain.Utilizador;

public class BubbleApplication {
	
	@Atomic
	public static void main(String[] args){
		System.out.println("Bem-Vindos ao BubbleDocs!!!");
		Bubbledocs bubble = Bubbledocs.getInstance();
		// v---Testes----v
		try{
			Utilizador u = bubble.obterUtilizador("si");
			FolhaCalculo f = new FolhaCalculo(u,"hoi",1,2);
			FolhaCalculo f1 = new FolhaCalculo(u,"hoi",1,2);
		}
		catch(Exception e){
			System.out.println(e);
		}
	}

}
