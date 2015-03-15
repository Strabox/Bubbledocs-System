package pt.tecnico.bubbledocs.domain;


import org.jdom2.Element;

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
    	deleteDomainObject();
    }
    
    public Element exportToXML(){
    	Element element = new Element("cell");
    	
    	element.setAttribute("line", Integer.toString(getLine()));
    	element.setAttribute("column", Integer.toString(getColumn()));
   
    	element.addContent(getContent().exportToXML());
   
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
    		return;
    	}
    	else if((content = element.getChild("mul")) != null){
    		MUL mul = new MUL();
    		mul.importFromXML(content);
    		setContent(mul);
    		return;
    	}
    	else if((content = element.getChild("sub")) != null){
    		SUB sub = new SUB();
    		sub.importFromXML(content);
    		setContent(sub);
    		return;
    	}
    	else if((content = element.getChild("add")) != null){
    		ADD add = new ADD();
    		add.importFromXML(content);
    		setContent(add);
    		return;
    	}
    	else if((content = element.getChild("numberint")) != null){
    		NumberInt n = new NumberInt();
    		n.importFromXML(content);
    		setContent(n);
    		return;
    	}
    	else if((content = element.getChild("reference")) != null){
    		Reference ref = new Reference();
    		ref.importFromXML(content);
    		setContent(ref);
    		return;
    	}
    }
    
}
