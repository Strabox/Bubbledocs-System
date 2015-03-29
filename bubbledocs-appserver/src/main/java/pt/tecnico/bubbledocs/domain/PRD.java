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
    
    public Element exportToXML(){
    	Element element = new Element("prd");
    	
    	element.setAttribute("top", Integer.toString(getTop()));
    	element.setAttribute("bottom", Integer.toString(getBottom()));
    	element.setAttribute("left", Integer.toString(getLeft()));
    	element.setAttribute("right", Integer.toString(getRight()));

    	return element;
    }
    
}
