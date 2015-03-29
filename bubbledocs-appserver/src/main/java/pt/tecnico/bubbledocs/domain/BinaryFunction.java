package pt.tecnico.bubbledocs.domain;

import java.util.List;

import org.jdom2.Element;

public abstract class BinaryFunction extends BinaryFunction_Base {
    
    public BinaryFunction() {
        super();
    }
    
    public void init(Content arg1,Content arg2){
    	setArgument1(arg1);
    	setArgument2(arg2);
    }
    
    @Override
	public int getResult(){
    	
		setResultado(calcula(getArgument1().getResult(),getArgument2().getResult()));
		return getResultado();
	}
    
    public void importFromXML(Element element) {
    	Element arg1;
    	Element arg2;
    	Element content;
    	List<Element> contents = element.getChildren();
    	
    	arg1 = contents.get(0);
    	if((content = arg1.getChild("literal")) != null){
    		Literal n = new Literal();
    		n.importFromXML(content);
    		setArgument1(n);  		
    	}
    	else if((content = arg1.getChild("reference")) != null){
    		Reference ref = new Reference();
    		ref.importFromXML(content);
    		ref.mountReference(getCell());
    		setArgument1(ref);
    	}
    	
    	arg2 = contents.get(1);
    	if((content = arg2.getChild("literal")) != null){
    		Literal n = new Literal();
    		n.importFromXML(content);
    		setArgument2(n);
    	}
    	else if((content = arg2.getChild("reference")) != null){
    		Reference ref = new Reference();
    		ref.importFromXML(content);
    		ref.mountReference(getCell());
    		setArgument2(ref);
    	}
    
    	return;
    }
    
    public void mountReference(Cell holder){
    	getArgument1().mountReference(holder);
    	getArgument2().mountReference(holder);
    }
    
    /* 
     * Calcula(int,int) - Subclasses implement the specific operation.
     */
    public abstract int calcula(int arg1,int arg2);
    
    /*
     * Delete() - Delete it from persistent state.
     */
    public void delete(){
    	getArgument1().delete();
    	getArgument2().delete();
    	setArgument1(null);
    	setArgument2(null);
    	super.delete();
    }
}
