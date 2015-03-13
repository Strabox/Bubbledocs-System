package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;


public class ADD extends ADD_Base {
    
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
    	
    	element.addContent(getArgument1().exportToXML());
    	element.addContent(getArgument2().exportToXML());

    	return element;
    }
    
    public void importFromXML(Element element, Cell container) {
    	return;
    }
}
