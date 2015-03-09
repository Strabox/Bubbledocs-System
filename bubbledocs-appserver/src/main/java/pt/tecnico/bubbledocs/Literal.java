package pt.tecnico.bubbledocs;

public class Literal extends Literal_Base {
    
    public Literal(int conteudo) {
        super();
        super.init(conteudo);
    }
    
    @Override
    public int getResult(){
    	return this.getResultado();
    }
    
}
