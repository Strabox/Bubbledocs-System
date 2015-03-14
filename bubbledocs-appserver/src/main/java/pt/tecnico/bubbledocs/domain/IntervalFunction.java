package pt.tecnico.bubbledocs.domain;

import java.util.Set;

public abstract class IntervalFunction extends IntervalFunction_Base {
    
    public IntervalFunction() {
        super();
    }
    public void init(Content args[]){
    	for(int i=0; i<args.length; i++){
    		addArgument(args[i]);
    	}
    }
    
    
    @Override
	public int getResult(){
    	Set<Content> aux = getArgumentSet();
    	int args[] = new int[aux.size()], i=0;
    	for(Content c: aux){
    		args[i] = c.getResult();
    		i++;
    	}
    	
		setResultado(calcula(args));
		return getResultado();
	}
    
    /* 
     * Calcula - As sublcasses implementam a operação especifica sobre os
     * argumentos.
     */
    public abstract int calcula(int args[]);
}
