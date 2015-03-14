package pt.tecnico.bubbledocs.domain;

import org.joda.time.LocalDate;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import pt.tecnico.bubbledocs.exceptions.OutOfSpreadsheetBoundariesException;

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
     * setLinhas - Overrided para que não se possa alterar o numero de linahs da folha.
     */
    @Override
    public void setLines(int lines){}
    
    /*
     * setColunas - Overrided para que não se possa alterar o numero de colunas da folha.
     */
    @Override
    public void setColumns(int columns){}
    
    /*
     * setId - Overrided para que não se possa alterar o ID atribuido pelo sistema.
     */
    @Override
    public void setId(int id){}
    
    /*
     * setDataCriacao - Overrided para que não se possa alterar a data de criacao da folha.
     */
    @Override
    public void setCreationDate(LocalDate date){}
    
    /*
     * Delete() - Delete Object from Persistent State.
     */
    void delete(){
    	this.setOwner(null);
    	this.setBubbledocsFolhas(null);
    	for(AcessType a : this.getTipoSet()){
    		a.delete();
    	}
    	for(Cell c: this.getCelSet()){
    		c.delete();
    	}
    	deleteDomainObject();
    }
    
    
    public void addContentToCell(int l, int c, Content cont){
    	if ( !( 0 <= l && l <getLines() && 0<= c && c <getColumns() ))
    		throw new OutOfSpreadsheetBoundariesException();
    	for(Cell cell : getCelSet()){
    		if (cell.getLine()==l && cell.getColumn()==c){
    			cell.setContent(cont);
    			return;
    		}
    	}
    }
    
    public Element exportToXML(){
    	org.jdom2.Document xmlout = new org.jdom2.Document();
    	Element element = new Element("spreadsheet");
    	
    	element.setAttribute("owner", getOwner().getName());

    	Element cells = new Element("cells");
    	element.addContent(cells);

    	for (Cell c : getCelSet()) {
    	    cells.addContent(c.exportToXML());
    	}
    	
    	xmlout.setRootElement(element);

    	XMLOutputter xml = new XMLOutputter();

    	xml.setFormat(Format.getPrettyFormat());

    	System.out.println(xml.outputString(element));

    	return element;
    }
    
    public void importFromXML(Element element) {
    	return;
    }
    
    @Override
    public String toString(){
    	String s ="ID: "+getId()+"\nNome: "+getName()+"\nData Criação: "+getCreationDate().toString()+"\nDono: "+getOwner().getName();
    	return s;
    }
}
