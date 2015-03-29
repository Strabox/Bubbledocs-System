package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

public class Reference extends Reference_Base {
    
	public Reference(){
		super();
	}
	
    public Reference(Cell referenciada,int line,int column) {
        super();
        this.setRefCell(referenciada);
        this.setLine(line);
        this.setColumn(column);
    }
    
    public Reference(int linha,int coluna) {
        super();
        this.setLine(linha);
        this.setColumn(coluna);
    }
    
    /*
     * Delete it from persistent state.
     */
    public void delete(){
    	this.setRefCell(null);
    	super.delete();
    }
    
    @Override
    public int getResult(){
    	return getRefCell().getResult();
    }
    
    public void mountReference(Cell cell){
    	SpreadSheet sheet = cell.getFc();
    	Cell r = sheet.getSingleCell(getLine(),getColumn());
    	if (r!=null){
    		setRefCell(r);
    		//System.out.println(getLine()+ " "+ getColumn() + "exists");
    	}
    	else{
    		r = new Cell(getLine(), getColumn());
    		sheet.addCell(r);
    		setRefCell(r);
    		//System.out.println(getLine()+ " "+ getColumn() + "created");
    	}
    }
    
    /*
     * Used to write a Reference as XML.
     */
    public Element exportToXML(){
    
    	Element element = new Element("reference");
    	element.setAttribute("line", Integer.toString(getLine()));
    	element.setAttribute("column", Integer.toString(getColumn()));
    	
    	return element;
    }
    
    public void importFromXML(Element element) {
    	setLine(Integer.parseInt(element.getAttribute("line").getValue()));
    	setColumn(Integer.parseInt(element.getAttribute("column").getValue()));
    }
    
    
}
