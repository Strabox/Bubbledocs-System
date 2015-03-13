package pt.tecnico.bubbledocs.domain;

public class Cell extends Cell_Base {
    
    public Cell(int l, int c,Content conteudo) {
        super();
        this.setContent(conteudo);
        setLinha(l);
        setColuna(c);
    }
    
    public void delete(){
    	  //FIX ME	
    	//deleteDomainObject();
    	
    }
    
}
