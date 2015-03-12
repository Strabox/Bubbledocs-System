package pt.tecnico.bubbledocs.domain;

public class Celula extends Celula_Base {
    
    public Celula(int l, int c,Conteudo conteudo) {
        super();
        this.setConteudo(conteudo);
        setLinha(l);
        setColuna(c);
    }
    
}
