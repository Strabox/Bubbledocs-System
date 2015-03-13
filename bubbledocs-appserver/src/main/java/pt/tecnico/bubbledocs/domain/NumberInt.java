package pt.tecnico.bubbledocs.domain;

public class NumberInt extends NumberInt_Base {
    
    public NumberInt(int conteudo) {
        super();
        super.init(conteudo);
    }
    
    @Override
    public int getResult(){
    	return this.getResultado();
    }
    
}
