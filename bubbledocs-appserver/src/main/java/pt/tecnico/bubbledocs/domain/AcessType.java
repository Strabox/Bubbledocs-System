package pt.tecnico.bubbledocs.domain;


public class AcessType extends AcessType_Base {
    
    public AcessType(SpreadSheet folha,AcessMode modo) {
        super(); 
        this.setMode(modo);
        this.setFolha(folha);
    }
    
}
