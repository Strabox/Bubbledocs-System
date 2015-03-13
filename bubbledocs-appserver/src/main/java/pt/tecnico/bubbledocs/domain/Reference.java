package pt.tecnico.bubbledocs.domain;

public class Reference extends Reference_Base {
    
    public Reference(Cell referenciada,int linha,int coluna) {
        super();
        this.setRefCell(referenciada);
        this.setLinha(linha);
        this.setColuna(coluna);
    }
    
    @Override
    public int getResult(){
    	setResultado(getRefCell().getContent().getResult());
    	return getResultado();
    }
    
}
