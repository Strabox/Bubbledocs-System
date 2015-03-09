package pt.tecnico.bubbledocs;

public abstract class FuncaoBinaria extends FuncaoBinaria_Base {
    
    public FuncaoBinaria() {
        super();
    }
    
    @Override
	public int getResult(){
    	// FIX-ME
		this.setResultado(calcula(1,1));
		return this.getResultado();
	}
    
    /* 
     * Calcula - As sublcasses implementam a operação especifica sobre os
     * argumentos.
     */
    public abstract int calcula(int arg1,int arg2);
    
}
