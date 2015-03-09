package pt.tecnico.bubbledocs;

public abstract class Conteudo extends Conteudo_Base {
    
    protected Conteudo() {
        super();
    }
    
    protected void init(int valor){
    	setResultado(valor);
    }
    
    public abstract int getResult();
    
    
}
