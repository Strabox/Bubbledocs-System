package pt.tecnico.bubbledocs.domain;

public abstract class BinaryFunction extends BinaryFunction_Base {
    
    public BinaryFunction() {
        super();
    }
    
    public void init(Content arg1,Content arg2){
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
    
    /*
     * Delete() - Delete it from persistent state.
     */
    public void delete(){
    	getArgument1().delete();
    	getArgument2().delete();
    	setCell(null);
    	deleteDomainObject();
    }
}
