package pt.tecnico.bubbledocs;

import org.joda.time.LocalDate;


public class FolhaCalculo extends FolhaCalculo_Base {
	
	
    public FolhaCalculo(Utilizador owner,String nome,int linhas,int colunas) {
        super();
        this.setNome(nome);
        this.setLinhas(linhas);
        this.setColunas(colunas);
        this.setOwner(owner);
        this.setDataCriacao(new LocalDate());
    }
	
    @Override
    public String toString(){
    	String s = "Nome: "+getNome()+"\nData Criação: "+getDataCriacao().toString()+"\nDono: "+getOwner().getNome();
    	return s;
    }
    
}
