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
    	if((content = arg1.getChild("numberint")) != null){
    		NumberInt n = new NumberInt();
    		n.importFromXML(content);
    		setArgument1(n);  		
    	}
    	else if((content = arg1.getChild("reference")) != null){
    		Reference ref = new Reference();
    		setArgument1(ref);
    		System.out.println(content.toString());
    		ref.importFromXML(content, getCell());
    	}
    	
    	arg2 = contents.get(1);
    	if((content = arg2.getChild("numberint")) != null){
    		NumberInt n = new NumberInt();
    		n.importFromXML(content);
    		setArgument2(n);
    	}
    	else if((content = arg2.getChild("reference")) != null){
    		Reference ref = new Reference();
    		setArgument2(ref);
    		ref.importFromXML(content, getCell());
    	}
    
    	return;
    }
    
    /* 
     * Calcula - Subclasses implement the specific operation.
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
    	setCell(null);
    	deleteDomainObject();
    }
}
