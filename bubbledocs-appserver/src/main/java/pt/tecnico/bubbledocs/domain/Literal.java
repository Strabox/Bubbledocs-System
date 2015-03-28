package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

public class Literal extends Literal_Base {
    
	public Literal(){
		super();
	}
	
    public Literal(int conteudo) {
        super();
        super.init(conteudo);
    }
    
    /*
     * Delete() - Delete instance from persistent state.
     */
    public void delete(){
    	this.setCell(null);
    	this.setBinaryFunction1(null);
    	this.setIntervalFunction(null);
    	this.setBinaryFunction2(null);
    	this.setIntervalFunction(null);
    	deleteDomainObject();
    }
    
    @Override
    public int getResult(){
    	return this.getResultado();
    }
    
    public void mountReference(Cell cell){
    	return;
    }
    
    public Element exportToXML(){
    	Element element = new Element("literal");
    	
    	element.setAttribute("value", Integer.toString(getResultado()));

    	return element;
    }
    
    public void importFromXML(Element element) {
    	int v = Integer.parseInt(element.getAttribute("value").getValue());
    	setResultado(v);
    }
    
    public void importFromXML(Element element,Cell container) {
    	return;
    }
}
