package pt.tecnico.bubbledocs.domain;

public abstract class Content extends Content_Base {
    
    protected Content() {
        super();
    }
    
    protected void init(int valor){
    	setResultado(valor);
    }
    
    public abstract int getResult();
    
    
}
