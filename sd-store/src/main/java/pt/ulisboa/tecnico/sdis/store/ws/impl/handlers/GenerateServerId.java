package pt.ulisboa.tecnico.sdis.store.ws.impl.handlers;

public class GenerateServerId {

	public static int id = 0;
	
	public static int getId(){
		id++;
		if(id == 3)
			id = 1;
		return id;
	}
	
	public static void reset(){
		id = 1;
	}
}
