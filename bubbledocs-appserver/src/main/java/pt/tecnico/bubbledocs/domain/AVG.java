package pt.tecnico.bubbledocs.domain;

public abstract class AVG extends AVG_Base {
    
    public AVG(Content args[]) {
        super();
        super.init(args);
    }
    
    public int calcula(int args[]){
    	int somatorio = 1;
    	for(int i=0;i<args.length;i++)
    		somatorio += args[i];
    	
    	return somatorio;
    }
}

