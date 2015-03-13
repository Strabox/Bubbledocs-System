package pt.tecnico.bubbledocs.domain;

public abstract class PRD extends PRD_Base {
    
	public PRD(Content[] args) {
        super();
        super.init(args);
    }
    
    public int calcula(int args[]){
    	int piatorio = 1;
    	for(int i=0;i<args.length;i++)
    		piatorio *= args[i];
    	
    	return piatorio;
    }
    
}
