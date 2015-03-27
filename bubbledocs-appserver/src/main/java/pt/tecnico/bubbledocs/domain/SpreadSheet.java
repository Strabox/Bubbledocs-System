package pt.tecnico.bubbledocs.domain;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.joda.time.LocalDate;
import org.jdom2.Element;

import pt.ist.fenixframework.Atomic;
import pt.tecnico.bubbledocs.exceptions.BadCellContentException;
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
     * Delete() - Delete Object from Persistent State.
     */
    @Atomic
    public void delete(){
    	this.setOwner(null);
    	this.setBubbledocsFolhas(null);
    	for(AccessType a : this.getTipoSet()){
    		a.delete();
    	}
    	for(Cell c: this.getCelSet()){
    		c.delete();
    	}
    	deleteDomainObject();
    }
    
    public Cell getSingleCell(int l, int c){
    	if ( !( 0 <= l && l <getLines() && 0<= c && c <getColumns() ))
    		throw new OutOfSpreadsheetBoundariesException();
    	for(Cell cell : getCelSet()){
    		if (cell.getLine()==l && cell.getColumn()==c){
    			return cell;
    		}
    	}
    	return null;
    	/* Esta funcao poderia devolver uma celula vazia, mas em caso de apenas se querer saber
    	 * se existe celula, isso podia estragar coisas.
    	Cell cell = new Cell(l, c);
    	addCel(cell);
    	return cell;
    	*/
    }
    
    public void addContentToCell(int l, int c, Content cont){
    	Cell cell = getSingleCell(l, c);
    	if (cell!=null){
			cell.setContent(cont);
			return;
    	}
    	cell = new Cell(l, c, cont);
    	cell.setContent(cont);
    	addCel(cell);
    	return;
    }
    
    public void addContentToCellFromString(int l, int c, String contentInString){
    	Content cont = factory(contentInString);
    	Cell cell = getSingleCell(l, c);
    	if (cell!=null){
			cell.setContent(cont);
			return;
    	}
    	cell = new Cell(l, c, cont);
    	cell.setContent(cont);
    	addCel(cell);
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

    	for (Cell c : getCelSet()) {
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
    	    Cell c = new Cell();
    	    this.addCel(c);
    	    c.importFromXML(cell);
    	}
    	return;
    }
    
    public Content factory(String input){
		input = input.replace("=","");
		String[] splited;
		splited = input.split("\\(|,|:|\\)");
		boolean isFGama=false;
		if(input.indexOf(":")!=-1) isFGama=true;
		try{
			if(splited.length == 3){							//Se for uma funcao.
				splited[0] = "calc."+splited[0]; 
				@SuppressWarnings("rawtypes")
				Class tipo = Class.forName(splited[0]);
				Constructor<?> ctor = tipo.getConstructors()[0];
				if(!isFGama){
					BinaryFunction b = (BinaryFunction) ctor.newInstance();
					b.init(factory("="+splited[1]),factory("="+splited[2]));
					return b;
				}else{ //se o argumento for uma gama
					//String stringArgGama = splited[1]+":"+splited[2];
					//Integer[] intArrayGama = splitGamas(stringArgGama); //separa em limites (linhas,colunas) da gama
					//Conteudo c = (Conteudo) ctor.newInstance( gamaToRefArray(intArrayGama), intArrayGama[0], intArrayGama[1], intArrayGama[2], intArrayGama[3]);
					//return c;
				}
			}
			splited = splited[0].split(";");
			if(splited.length == 1)								//Se for Literal.
				return new NumberInt(Integer.parseInt(splited[0]));
			
			else if(splited.length == 2){						//Se for Reference.
				Integer Linha = Integer.parseInt(splited[0]);
				Integer Coluna = Integer.parseInt(splited[1]);
				if(getSingleCell(Linha,Coluna) == null){
					Cell c = new Cell(Linha, Coluna);
					addCel(c);
					return new Reference(c, Linha, Coluna);
				}
				else
					return new Reference(getSingleCell(Linha,Coluna),Linha,Coluna);
			}
		}
		catch (ClassNotFoundException exc) {
			System.err.println("CLASS NOT FOUND");
			System.err.println(exc);
			throw new BadCellContentException(input);
		}
		catch (InstantiationException e) {
			System.err.println(e);
			throw new BadCellContentException(input);
		}
		catch (IllegalAccessException e) {
			System.err.println(e);
			throw new BadCellContentException(input);
		}
		catch (IllegalArgumentException e) {
			System.err.println(e);
			throw new BadCellContentException(input);
		}
		catch (InvocationTargetException e) {
			System.err.println(e);
			throw new BadCellContentException(input);
		}
		throw new BadCellContentException(input);
	}
    
    @Override
    public String toString(){
    	String s ="Nome: "+getName()+"\nID: "+getId();
    	return s;
    }
}
