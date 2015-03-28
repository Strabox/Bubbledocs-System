package pt.tecnico.bubbledocs.domain;


public class Permission extends Permission_Base {
    
    public Permission(SpreadSheet folha,AccessMode modo) {
        super(); 
        this.setMode(modo);
        this.setSpreadsheet(folha);
    }
    
    /*
     * delete() - Delete it from persistent state. 
     */
    public void delete(){
    	setUses(null);
    	setSpreadsheet(null);
    	deleteDomainObject();
    }
    
}
