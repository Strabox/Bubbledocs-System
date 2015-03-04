package pt.tecnico.bubbledocs;

import pt.ist.fenixframework.FenixFramework;

public class FolhaCalculo extends FolhaCalculo_Base {
	
    private FolhaCalculo(String g) {
        super();
        FenixFramework.getDomainRoot().setFolhaCalculo(this);
        setH(g);
    }
    
    public static FolhaCalculo getInstance(){
    	FolhaCalculo folha = FenixFramework.getDomainRoot().getFolhaCalculo();
    	if(folha == null)
    		folha = new FolhaCalculo("a");
    	return folha;
    }
    
}
