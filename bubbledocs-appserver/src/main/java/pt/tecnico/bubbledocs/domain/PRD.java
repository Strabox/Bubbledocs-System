package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

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
    
    public void importFromXML(Element element, Cell container) {
    	return;
    }
}
