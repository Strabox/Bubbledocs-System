package pt.tecnico.bubbledocs.domain;


public class AcessType extends AcessType_Base {
    
    public AcessType(SpreadSheet folha,AcessMode modo) {
        super(); 
        this.setMode(modo);
        this.setFolha(folha);
    }
    
    /*
     * delete() - Delete it from persistent state. 
     */
    public void delete(){
    	setUses(null);
    	setFolha(null);
    }
    
}
