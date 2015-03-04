package pt.tecnico.bubbledocs;

import pt.ist.fenixframework.Atomic;

public class BubbleApplication {
	
	@Atomic
	public static void main(String[] args){
		System.out.println("Bem-Vindos ao BubbleDocs!!!");
		
		FolhaCalculo folha = FolhaCalculo.getInstance();
	}

}
