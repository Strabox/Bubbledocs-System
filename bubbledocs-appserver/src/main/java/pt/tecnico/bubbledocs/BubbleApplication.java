package pt.tecnico.bubbledocs;

import pt.ist.fenixframework.Atomic;

public class BubbleApplication {
	
	@Atomic
	public static void main(String[] args){
		System.out.println("Bem-Vindos ao BubbleDocs!!!");
		
		SistemaLogin l = SistemaLogin.getInstance();
		FolhaCalculo f = new FolhaCalculo("Hoi");
		System.out.println(f.getDataCriacao());
		System.out.println(f.getId());
		FolhaCalculo f1 = new FolhaCalculo("Hoi");
		System.out.println(f1.getId());
	}

}
