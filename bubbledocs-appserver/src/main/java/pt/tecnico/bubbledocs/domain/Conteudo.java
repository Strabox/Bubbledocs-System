package pt.tecnico.bubbledocs.domain;

public abstract class Conteudo extends Conteudo_Base {
    
    protected Conteudo() {
        super();
    }
    
    protected void init(int valor){
    	setResultado(valor);
    }
    
    public abstract int getResult();
    
    
}
