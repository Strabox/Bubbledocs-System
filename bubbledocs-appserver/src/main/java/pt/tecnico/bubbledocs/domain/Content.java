package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

public abstract class Content extends Content_Base {
    
    protected Content() {
        super();
    }
    
    protected void init(int valor){
    	setResultado(valor);
    }
    
    public abstract int getResult();
    
    public abstract Element exportToXML();

    public abstract void importFromXML(Element element, Cell container);
    
    public abstract void delete();
}
