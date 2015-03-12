package pt.tecnico.bubbledocs.domain;

public abstract class FuncaoBinaria extends FuncaoBinaria_Base {
    
    public FuncaoBinaria() {
        super();
    }
    
    public void init(Conteudo arg1,Conteudo arg2){
    	setArgument1(arg1);
    	setArgument2(arg2);
    }
    
    @Override
	public int getResult(){
    	
		setResultado(calcula(getArgument1().getResult(),getArgument2().getResult()));
		return getResultado();
	}
    
    /* 
     * Calcula - As sublcasses implementam a operação especifica sobre os
     * argumentos.
     */
    public abstract int calcula(int arg1,int arg2);
    
}
