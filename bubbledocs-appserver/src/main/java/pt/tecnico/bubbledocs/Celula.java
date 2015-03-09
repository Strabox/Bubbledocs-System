package pt.tecnico.bubbledocs;

public class Celula extends Celula_Base {
    
    public Celula(Conteudo conteudo) {
        super();
        this.setConteudo(conteudo);
    }
    
    public void apagarConteudo(){
    	setConteudo(null);
    }
    
    
}
