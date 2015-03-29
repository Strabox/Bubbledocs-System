package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

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
    
    public Element exportToXML(){
    	Element element = new Element("avg");
    	
    	element.setAttribute("top", Integer.toString(getTop()));
    	element.setAttribute("bottom", Integer.toString(getBottom()));
    	element.setAttribute("left", Integer.toString(getLeft()));
    	element.setAttribute("right", Integer.toString(getRight()));

    	return element;
    }
    
}

