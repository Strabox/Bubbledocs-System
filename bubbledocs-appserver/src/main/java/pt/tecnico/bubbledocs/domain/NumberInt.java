package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

public class NumberInt extends NumberInt_Base {
    
    public NumberInt(int conteudo) {
        super();
        super.init(conteudo);
    }
    
    @Override
    public int getResult(){
    	return this.getResultado();
    }
    
    public Element exportToXML(){
    	Element element = new Element("numberint");
    	
    	element.setAttribute("value", Integer.toString(getResultado()));

    	return element;
    }
    
    public void importFromXML(Element element, Cell container) {
    	int v = Integer.parseInt(element.getAttribute("value").getValue());
    	setResultado(v);
    }
}
