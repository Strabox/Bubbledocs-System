package pt.tecnico.bubbledocs.domain;

public class Referencia extends Referencia_Base {
    
    public Referencia(Celula referenciada,int linha,int coluna) {
        super();
        this.setRefcelula(referenciada);
        this.setLinha(linha);
        this.setColuna(coluna);
    }
    
    @Override
    public int getResult(){
    	setResultado(getCelula().getConteudo().getResult());
    	return getResultado();
    }
    
}
