package pt.tecnico.bubbledocs;

import pt.tecnico.bubbledocs.integration.CreateUserIntegrator;
import pt.tecnico.bubbledocs.service.local.LoginUser;


public class BubbleApplication {
	
    /* main - Bubbledocs main function. */
	public static void main(String[] args){
		System.out.println("Bem-Vindos ao BubbleDocs!!!");		
		/* This wwith a true SD-ID server. 
		try{
			LoginUser lu = new LoginUser("root", "root");
			lu.execute();
			String token = lu.getUserToken();
			CreateUserIntegrator c = new CreateUserIntegrator(token, "Andre p", "parda@a.a", "andre");
			c.execute();
		}catch(Exception e){
			System.out.println(e);
		}*/
		
	}

}
