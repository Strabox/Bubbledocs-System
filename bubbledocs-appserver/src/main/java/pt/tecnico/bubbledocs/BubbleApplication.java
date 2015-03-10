package pt.tecnico.bubbledocs;

import pt.ist.fenixframework.Atomic;

public class BubbleApplication {
	
	@Atomic
	public static void main(String[] args){
		System.out.println("Bem-Vindos ao BubbleDocs!!!");
		Bubbledocs bubble = Bubbledocs.getInstance();
		// v---Testes----v
		try{
			Utilizador u = new Utilizador("Badeh","Bad","badutss");
			FolhaCalculo f = new FolhaCalculo(u,"Coisa",22,22);
			u.listarFolhas();
			bubble.adicionaUtilizador(u);
			bubble.listarUtilizadores();
			bubble.listarFolhas();
		}
		catch(Exception e){
			System.out.println(e);
		}
	}

}
