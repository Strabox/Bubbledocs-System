package pt.tecnico.bubbledocs.domain;

import org.jdom2.Element;

public class Reference extends Reference_Base {
    
    public Reference(Cell referenciada,int linha,int coluna) {
        super();
        this.setRefCell(referenciada);
        this.setLine(linha);
        this.setColumn(coluna);
    }
    
    /*
     * Delete it from persistent state.
     */
    public void delete(){
    	this.setRefCell(null);
    	this.setCell(null);
    	deleteDomainObject();
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
    
    public void importFromXML(Element element, Cell container) {
    	int column = Integer.parseInt(element.getAttribute("line").getValue());
    	int line = Integer.parseInt(element.getAttribute("column").getValue());
    	setLine(line);
    	setColumn(column);
    	Cell c;
    	// Checks if there already is a cell in the spreadsheet (accessed using the containing cell.
    	c = container.getFc().getSingleCell(line, column);
    	if(c != null){
    		setRefCell(c);
    		return; // Success: sets the cell as referred to, then exits.
    	}
    	//Failure: creates an "empty" cell that may later have a content
    	c = new Cell(line, column);
    	container.getFc().addCel(c);
    	setRefCell(c);
    }
}
