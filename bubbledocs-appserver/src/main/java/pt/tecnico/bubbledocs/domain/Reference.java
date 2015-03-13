package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

public class Reference extends Reference_Base {
    
    public Reference(Cell referenciada,int linha,int coluna) {
        super();
        this.setRefCell(referenciada);
        this.setLine(linha);
        this.setColumn(coluna);
    }
    
    @Override
    public int getResult(){
    	setResultado(getRefCell().getContent().getResult());
    	return getResultado();
    }
    
    public Element exportToXML(){
    	Element element = new Element("reference");
    	
    	element.setAttribute("line", Integer.toString(getRefCell().getLine()));
    	element.setAttribute("column", Integer.toString(getRefCell().getColumn()));

    	return element;
    }
}
