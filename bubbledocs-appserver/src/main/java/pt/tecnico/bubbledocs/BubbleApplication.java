package pt.tecnico.bubbledocs;

import pt.ist.fenixframework.Atomic;
import pt.tecnico.bubbledocs.domain.*;

import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class BubbleApplication {
	
	@Atomic
	public static void main(String[] args){
		System.out.println("Bem-Vindos ao BubbleDocs!!!");
		Bubbledocs bubble = Bubbledocs.getInstance();
		// v---Testes----v
		try{
			Utilizador u = bubble.obterUtilizador("si");
			FolhaCalculo f = new FolhaCalculo("hoi",99,99);
			Celula cel = new Celula (1,1,new Literal(3));
			Celula cel1 = new Celula (3,3,new Literal(8));
			Celula celm = new Celula (4,4,new MUL(new Referencia(cel,1,1),new Literal (7)));
			

			f.addCel(cel);
			f.addCel(cel1);
			f.addCel(celm);
			
			
			
		}
		catch(Exception e){
			System.out.println(e);
		}
	}

}
