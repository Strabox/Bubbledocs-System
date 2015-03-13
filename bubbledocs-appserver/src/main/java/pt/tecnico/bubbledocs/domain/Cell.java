package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

public class Cell extends Cell_Base {
    
    public Cell(int l, int c,Content conteudo) {
        super();
        this.setContent(conteudo);
        setLine(l);
        setColumn(c);
    }
    
    public Cell(int l, int c) {
        super();
        setLine(l);
        setColumn(c);
    }
    
    public void delete(){
    	  //FIX ME	
    	//deleteDomainObject();
    	
    }
    
    public Element exportToXML(){
    	Element element = new Element("spreadsheet");
    	
    	element.setAttribute("line", Integer.toString(getLine()));
    	element.setAttribute("column", Integer.toString(getColumn()));

    	element.addContent(getContent().exportToXML());

    	return element;
    }
    
    public void importFromXML(Element element) {
    	return;
    }
}
