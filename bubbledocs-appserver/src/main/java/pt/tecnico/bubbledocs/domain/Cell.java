package pt.tecnico.bubbledocs.domain;


import org.jdom2.Element;

import pt.tecnico.bubbledocs.exceptions.NoValueForReferenceException;

public class Cell extends Cell_Base {
    
	public Cell(){
		super();
	}
	
    public Cell(int l, int c,Content conteudo) {
        super();
        setContent(conteudo);
        setLine(l);
        setProtect(false);
        setColumn(c);
    }
    
    public Cell(int l, int c) {
        super();
        setLine(l);
        setColumn(c);
        setProtect(false);
    }
    
    public void delete(){
    	setFc(null);
    	getContent().delete();
    	this.setContent(null);
    	this.setReference(null);
    	deleteDomainObject();
    }
    
    public int getResult(){
    	if(getContent()==null) throw new NoValueForReferenceException();
    	return getContent().getResult();
    }
    
    public Element exportToXML(){
    	Element element = new Element("cell");
    	
    	element.setAttribute("line", Integer.toString(getLine()));
    	element.setAttribute("column", Integer.toString(getColumn()));
    	
    	Content cont = getContent();
    	Element contexp = cont.exportToXML();
    	element.addContent(contexp);
    	
    	return element;
    }
    
    public void importFromXML(Element element) {
    	Element content;
    	this.setLine(Integer.parseInt(element.getAttribute("line").getValue()));
    	this.setColumn(Integer.parseInt(element.getAttribute("column").getValue()));
    	
    	if((content = element.getChild("div")) != null){
    		DIV div = new DIV();
    		div.importFromXML(content);
    		setContent(div);
    		div.mountReference(this);
    		return;
    	}
    	else if((content = element.getChild("mul")) != null){
    		MUL mul = new MUL();
    		mul.importFromXML(content);
    		setContent(mul);
    		mul.mountReference(this);
    		return;
    	}
    	else if((content = element.getChild("sub")) != null){
    		SUB sub = new SUB();
    		sub.importFromXML(content);
    		setContent(sub);
    		sub.mountReference(this);
    		return;
    	}
    	else if((content = element.getChild("add")) != null){
    		ADD add = new ADD();
    		add.importFromXML(content);
    		setContent(add);
    		add.mountReference(this);
    		return;
    	}
    	else if((content = element.getChild("literal")) != null){
    		Literal n = new Literal();
    		n.importFromXML(content);
    		setContent(n);
    		n.mountReference(this);
    		return;
    	}
    	else if((content = element.getChild("reference")) != null){
    		Reference ref = new Reference();
    		ref.importFromXML(content);
    		setContent(ref);
    		ref.mountReference(this);
    		return;
    	}
    }
    
    String resultToString(){
    	try{
    		int res = getContent().getResult();
    		return Integer.toString(res);
    	}
    	catch(Exception e){
    		return "()";
    	}
    }
    
}
