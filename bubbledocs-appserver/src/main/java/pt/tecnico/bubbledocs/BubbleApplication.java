package pt.tecnico.bubbledocs;

import pt.ist.fenixframework.Atomic;

public class BubbleApplication {
	
	@Atomic
	public static void main(String[] args){
		System.out.println("Bem-Vindos ao BubbleDocs!!!");
		Bubbledocs bubble = Bubbledocs.getInstance();
		// v---Testes----v
		try{
			Utilizador u = new Utilizador("Nome","Ai","badutss");
			Utilizador u2 = new Utilizador("Nome","Ab","badutss");
			u2.setUsername("hua");
			
		}
		catch(Exception e){
			System.out.println(e);
		}
	}

}
