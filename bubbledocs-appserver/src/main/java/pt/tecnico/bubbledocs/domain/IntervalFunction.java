package pt.tecnico.bubbledocs.domain;

import java.util.Set;
import java.util.Vector;

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
    	//int a = aux.size();
    	int args[];
    	for(int i=0; i< aux.size(); i++){
    		args[i] = aux[i];
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
