package pt.tecnico.bubbledocs;

import pt.ist.fenixframework.Atomic;
import pt.tecnico.bubbledocs.domain.*;

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
			ADD a = new ADD(new Literal(1),new Literal(2));
		}
		catch(Exception e){
			System.out.println(e);
		}
	}

}
