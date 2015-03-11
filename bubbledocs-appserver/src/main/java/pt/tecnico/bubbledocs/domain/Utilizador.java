package pt.tecnico.bubbledocs.domain;

import pt.tecnico.bubbledocs.exceptions.UsernameAlreadyTakenException;

import java.util.ArrayList;

public class Utilizador extends Utilizador_Base {
    
	/*
     * Throws unchecked exception UsernameAlreadyTakenException !!!!
     */
    public Utilizador(String nome,String username,String password){
    	super();
    	try{
			setNome(nome);
			setUsername(username);
			setPassword(password);
			setBubbledocsUtilizadores(Bubbledocs.getInstance());
    	}
    	catch(UsernameAlreadyTakenException e){
    		delete();
    		throw new UsernameAlreadyTakenException(username);
    	}
    }
    
    /*
     * 
     */
    @Override
    public void setUsername(String newUsername){
    	if(newUsername != null){
	    	for(Utilizador user: Bubbledocs.getInstance().getUtilizadorSet()){
	    		if(user.getUsername().equals(newUsername)){
	    			throw new UsernameAlreadyTakenException(newUsername);
	    		}
	    	}
	    	super.setUsername(newUsername);
    	}
    }
    
    /*
     * 
     */
    public void delete(){
    	//FIX-ME!!!!!!!!!!!!!
    	deleteDomainObject();
    }
    
    /*
     * obterFolhaPorNome - ...
     */
    public ArrayList<FolhaCalculo> obterFolhaPorNome(String nome){
    	ArrayList<FolhaCalculo> folhas = new ArrayList<FolhaCalculo>();
    	for(FolhaCalculo f: getOwnedSet()){
    		if(f.getNome().equals(nome))
    			folhas.add(f);
    	}
    	return folhas;
    }
    
    /*
     * listarFolhasUsadas - Retorna todas as folhas usadas pelo utilizador.
     */
    public ArrayList<FolhaCalculo> listarFolhasUsadas(){
    	ArrayList<FolhaCalculo> folhas = new ArrayList<FolhaCalculo>();
    	for(TipoAcesso t: this.getUsedBySet())
    		folhas.add(t.getFolha());
    	return folhas;
    }
    
    /*
     * listarFolhasCriadas - Retorna todas as folhas criadas pelo utilizador.
     */
    public ArrayList<FolhaCalculo> listarFolhasCriadas(){
    	ArrayList<FolhaCalculo> folhas = new ArrayList<FolhaCalculo>();
    	for(FolhaCalculo f: getOwnedSet()){
    		folhas.add(f);
    		System.out.println(f);
    	}
    	return folhas;
    }
    
    @Override
    public String toString(){
    	String s = "Nome: "+getNome()+"\nUsername: "+getUsername()+"\n";
    	return s;
    }
    
}
