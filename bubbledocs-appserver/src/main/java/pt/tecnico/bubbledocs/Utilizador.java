package pt.tecnico.bubbledocs;

public class Utilizador extends Utilizador_Base {
    
    public Utilizador(String nome,String username,String password) {
        super();
        setNome(nome);
        setUsername(username);
        setPassword(password);
    }
   
    public void listarFolhas(){
    	for(FolhaCalculo f: getOwnedSet())
    		System.out.println(f);
    }
    
    @Override
    public String toString(){
    	String s = "Nome: "+getNome()+"\nUsername: "+getUsername()+"\n";
    	return s;
    }
    
}
