package pt.tecnico.bubbledocs;

import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import pt.ist.fenixframework.Atomic;
import pt.tecnico.bubbledocs.domain.*;

public class BubbleApplication {
	
	@Atomic
	public static void main(String[] args){
		System.out.println("Bem-Vindos ao BubbleDocs!!!");
		Bubbledocs bubble = Bubbledocs.getInstance();
		// v---Testes----v
		try{
			new User("aa","aaa","");
			
			SpreadSheet f = new SpreadSheet("hoi",99,99);
			Cell cel = new Cell (1,1,new NumberInt(3));
			Cell cel1 = new Cell (3,3,new NumberInt(8));
			Cell celm = new Cell (4,4,new MUL(new Reference(cel,1,1),new NumberInt (7)));
			
	
			f.addCel(cel);
			f.addCel(cel1);
			f.addCel(celm);
			
			
			for(Cell _cell : f.getCelSet()) {
				System.out.printf("Linha:%d  Coluna:%d Result:%d\n",_cell.getLine(), _cell.getColumn(),_cell.getContent().getResult());
				
			}
			System.out.println ("Deleting");
			for(Cell _cell : f.getCelSet()) {
				_cell.delete();
				
			}
			System.out.println ("Show");
			for(Cell _cell : f.getCelSet()) {
				System.out.printf("Linha:%d  Coluna:%d Result:%d\n",_cell.getLine(), _cell.getColumn(),_cell.getContent().getResult());
				
			}
		}
		catch(Exception e){
			System.out.println(e);
		}

		/*		
		org.jdom2.Document doc = convertToXML();

		printDomainInXML(doc);

		

		org.jdom2.Document doc2 = convertToXML();

		printDomainInXML(doc2);

		recoverFromBackup(doc);

		doc2 = convertToXML();

		printDomainInXML(doc2);
		*/
	}
	/*
	public static void printDomainInXML(org.jdom2.Document jdomDoc) {
		XMLOutputter xml = new XMLOutputter();
		xml.setFormat(Format.getPrettyFormat());
		System.out.println(xml.outputString(jdomDoc));
	    }
	private static void recoverFromBackup(org.jdom2.Document jdomDoc) {
		Bubbledocs bubble = Bubbledocs.getInstance();

		bubble.importFromXML(jdomDoc.getRootElement());
	    }
	   @Atomic
	    public static org.jdom2.Document convertToXML() {
		Bubbledocs bubble = Bubbledocs.getInstance();
		
		org.jdom2.Document jdomDoc = new org.jdom2.Document();

		jdomDoc.setRootElement(bubble.exportToXML());

		return jdomDoc;
	    }
*/
}
