package pt.tecnico.bubbledocs.domain;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

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
    
    public SpreadSheet(){
    	super();
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
    public void delete(){
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
    	addCel(cell);
    }
    
    public org.jdom2.Document exportToXML(){
    	org.jdom2.Document xmlout = new org.jdom2.Document();
    	Element element = new Element("spreadsheet");
    	
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
    
    public void importFromXML(org.jdom2.Document doc) {
    	Element sheet = doc.getRootElement();
    	List<Element> cells = sheet.getChildren();
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
			return null;
		}
		catch (InstantiationException e) {
			System.err.println(e);
			return null;
		}
		catch (IllegalAccessException e) {
			System.err.println(e);
			return null;
		}
		catch (IllegalArgumentException e) {
			System.err.println(e);
			return null;
		}
		catch (InvocationTargetException e) {
			System.err.println(e);
			return null;
		}
		return null;
	}
    
    @Override
    public String toString(){
    	String s ="ID: "+getId()+"\nNome: "+getName()+"\nData Criação: "+getCreationDate().toString()+"\nDono: "+getOwner().getName();
    	return s;
    }
}
