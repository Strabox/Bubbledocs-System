package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;


public class ADD extends ADD_Base {
    
	public ADD(){
		super();
	}
	
    public ADD(Content arg1,Content arg2) {
        super();
        super.init(arg1, arg2);
    }
    
    @Override
    public int calcula(int arg1,int arg2){
    	return arg1 + arg2;
    }
    
    public Element exportToXML(){
    	Element element = new Element("add");
    	Element arg1 = new Element("argument1");
    	Element arg2 = new Element("argument2");
    	element.addContent(arg1);
    	element.addContent(arg2);
    	
    	arg1.addContent(getArgument1().exportToXML());
    	arg2.addContent(getArgument2().exportToXML());

    	return element;
    }
    
    
}
