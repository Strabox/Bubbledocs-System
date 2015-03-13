package pt.tecnico.bubbledocs;

import pt.ist.fenixframework.Atomic;
import pt.tecnico.bubbledocs.domain.*;

public class BubbleApplication {
	
	/* setupIfNeed - Setup the initial state if BubbleDocs is empty. */
    private static void setupIfNeed(Bubbledocs b) {
		if (b.getFolhaCalculoSet().isEmpty() && b.getUtilizadorSet().isEmpty())
		    SetupBubbledocs.populateDomain();
    }
	
    /* main - Bubbledocs main function. */
	@Atomic
	public static void main(String[] args){
		System.out.println("Bem-Vindos ao BubbleDocs!!!");
	
		Bubbledocs bubble = Bubbledocs.getInstance();
		/* Se for a 1º vez a correr ele vai preencher a BD com o necessário.*/
		setupIfNeed(bubble);				
		
		try{
			/*======== Espaço que contém as chamadas para visualizacao da 1ºEntrega ======*/
			
			bubble.listAllUsers();
			
			User pf = bubble.getUserByName("pf");
			User ra = bubble.getUserByName("ra");
			
			pf.listAllSpreadSheets();
			ra.listAllSpreadSheets();
			
			//FIX-ME Export SpreadSheet!!!
			
			//FIX-ME Remove SpreadSheet From persistent State!!!
			
			//FIX-ME Import SpreadSheet
			
			pf.listAllSpreadSheets();
		
			System.out.println("??????????????Fim Para Efeitos de Debug??????????????????????");
			/*============================================================================*/
			
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
