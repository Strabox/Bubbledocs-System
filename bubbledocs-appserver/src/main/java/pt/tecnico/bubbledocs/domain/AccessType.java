package pt.tecnico.bubbledocs.domain;


public class AccessType extends AccessType_Base {
    
    public AccessType(SpreadSheet folha,AccessMode modo) {
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
    	deleteDomainObject();
    }
    
}
