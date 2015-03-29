package pt.tecnico.bubbledocs.domain;

import org.joda.time.LocalDate;
import org.jdom2.Element;

import pt.ist.fenixframework.Atomic;
import pt.tecnico.bubbledocs.exceptions.OutOfSpreadsheetBoundariesException;
import pt.tecnico.bubbledocs.exceptions.UnauthorizedOperationException;

public class SpreadSheet extends SpreadSheet_Base {
	
	 
    public SpreadSheet(String name,int linhas,int colunas) {
        super();
        this.setName(name);
        super.setLines(linhas);
        super.setColumns(colunas);
        super.setCreationDate(new LocalDate());
        super.setId(Bubbledocs.getInstance().generateUniqueId());
    }
    
    /*
     * Necessary for XMLImport functions.
     */
    public SpreadSheet(){
    	super();
    	super.setCreationDate(new LocalDate());
        super.setId(Bubbledocs.getInstance().generateUniqueId());
    }
	
    /*
     * setLines - Overrided because the number of lines
     * cant change after creation.
     */
    @Override
    public void setLines(int lines){}
    
    /*
     * setColumns - Overrided because the number of columns
     * cant change after creation.
     */
    @Override
    public void setColumns(int columns){}
    
    /*
     * setId - Overrided so we cant mess with 
     * UniqueId generation.
     */
    @Override
    public void setId(int id){}
    
    /*
     * setCreationDate - Overrided because we cant change
     * creationDate after spreadsheet creation.
     */
    @Override
    public void setCreationDate(LocalDate date){}
    
    /*
     * changeUsers(String) - Method used to change spreadsheet's user set.
     */
    public void changeUsers(String username,String userToRemove){
    	boolean canChange = false;
    	for(Permission p : getPermissionSet()){
    		if(p.getUses().getUsername() == username && p.getMode() == AccessMode.WRITE){
    			canChange = true;
    		}
    	}
    	if(getOwner().getUsername() == username)
    		canChange = true;
    	if(!canChange)
    		throw new UnauthorizedOperationException();
    	
    	for(Permission p : getPermissionSet()){
    		if(p.getUses().getUsername() == userToRemove){
    			p.delete();
    		}
    	}
    }
    
    
    /*
     * Delete() - Delete Object from Persistent State.
     */
    @Atomic
    public void delete(){
    	this.setOwner(null);
    	this.setBubbledocsSpreadsheets(null);
    	for(Permission a : this.getPermissionSet()){
    		a.delete();
    	}
    	for(Cell c: this.getCellSet()){
    		c.delete();
    	}
    	deleteDomainObject();
    }
    
    public Cell getSingleCell(int l, int c){
    	if ( !( 0 <= l && l <getLines() && 0<= c && c <getColumns() ))
    		throw new OutOfSpreadsheetBoundariesException();
    	for(Cell cell : getCellSet()){
    		if (cell.getLine()==l && cell.getColumn()==c){
    			return cell;
    		}
    	}
    	return null;
    }
    
    public void addContentToCell(int l, int c, Content cont){
    	Cell cell = getSingleCell(l, c);
    	if (cell!=null){
			cell.setContent(cont);
			return;
    	}
    	cell = new Cell(l, c, cont);
    	cell.setContent(cont);
    	addCell(cell);
    	return;
    }
    
    public void addReferenceToCell(int lholder, int cholder, int lref, int cref){
    	//creates content
    	Reference ref;
    	if(getSingleCell(lref,cref) == null){
			Cell c = new Cell(lref, cref);
			addCell(c);
			ref = new Reference(c, lref, cref);
		}
		else{
			ref =  new Reference(getSingleCell(lref,cref),lref,cref);
		}
    	//adds to cell
    	Cell cell = getSingleCell(lholder, cholder);
    	if (cell!=null){
			cell.setContent(ref);
			return;
    	}
    	cell = new Cell(lholder, cholder, ref);
    	addCell(cell);
    	return;
    }
    
    @Atomic
    public org.jdom2.Document exportToXML(){
    	org.jdom2.Document xmlout = new org.jdom2.Document();
    	Element element = new Element("spreadsheet");
    	
    	element.setAttribute("name", getName());
    	element.setAttribute("owner", getOwner().getUsername());
    	element.setAttribute("lines", Integer.toString(getLines()));
    	element.setAttribute("columns", Integer.toString(getColumns()));

    	Element cells = new Element("cells");
    	element.addContent(cells);

    	for (Cell c : getCellSet()) {
    	    cells.addContent(c.exportToXML());
    	}
    	
    	xmlout.setRootElement(element);
    	return xmlout;
    }
    
    @Atomic
    public void importFromXML(org.jdom2.Document doc,String username) {
    	Element sheet = doc.getRootElement();
    	Element cells = sheet.getChild("cells");
    	String owner = sheet.getAttribute("owner").getValue();
    	setName(sheet.getAttribute("name").getValue());
    	super.setLines(Integer.parseInt(sheet.getAttribute("lines").getValue()));
    	super.setColumns(Integer.parseInt(sheet.getAttribute("columns").getValue()));
    	/*The user want import the spreadsheet isnt his owner */
    	if(owner != username)
    		new UnauthorizedOperationException();
     	
    	for (Element cell : cells.getChildren("cell")) {
    		Cell c;
    		int lin = Integer.parseInt(cell.getAttribute("line").getValue());
        	int col = Integer.parseInt(cell.getAttribute("column").getValue());
        	if((c = getSingleCell(lin,col))==null){
        		c = new Cell();
        		this.addCell(c);
        		
        	}
    	    c.importFromXML(cell);
    	    
    	}
    	return;
    }
    
    
    @Override
    public String toString(){
    	String s ="Nome: "+getName()+"\nID: "+getId();
    	return s;
    }
}
