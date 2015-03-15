package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

public class DIV extends DIV_Base {
    
	public DIV(){
		super();
	}
	
    public DIV(Content arg1,Content arg2) {
        super();
        super.init(arg1, arg2);
    }
    
    @Override
    public int calcula(int arg1,int arg2){
    	return arg1 / arg2;
    }
    
    public Element exportToXML(){
    	Element element = new Element("div");
    	Element arg1 = new Element("argument1");
    	Element arg2 = new Element("argument2");
    	element.addContent(arg1);
    	element.addContent(arg2);
    	System.out.println(element.toString());
    	arg1.addContent(getArgument1().exportToXML());
    	arg2.addContent(getArgument2().exportToXML());

    	System.out.println(arg1.toString());
    	return element;
    }
    
    
    public void importFromXML(Element element, Cell container) {
    	return;
    }
}
