package pt.tecnico.bubbledocs.domain;


public class TipoAcesso extends TipoAcesso_Base {
    
    public TipoAcesso(FolhaCalculo folha,ModoAcesso modo) {
        super(); 
        this.setModo(modo);
        this.setFolha(folha);
    }
    
}
