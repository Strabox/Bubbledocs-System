package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

public class NumberInt extends NumberInt_Base {
    
	public NumberInt(){
		super();
	}
	
    public NumberInt(int conteudo) {
        super();
        super.init(conteudo);
    }
    
    /*
     * Delete() - Delete instance from persistent state.
     */
    public void delete(){
    	this.setCell(null);
    	this.setFuncaoBinaria1(null);
    	this.setFuncaoIntervalo(null);
    	this.setFuncaoBinaria2(null);
    	this.setFuncaoIntervalo(null);
    	deleteDomainObject();
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
    
    public void importFromXML(Element element) {
    	int v = Integer.parseInt(element.getAttribute("value").getValue());
    	setResultado(v);
    }
    
    public void importFromXML(Element element,Cell container) {
    	return;
    }
}
