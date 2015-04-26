package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.service.local.AssignReferenceCell;

public class AssignReferenceCellIntegrator extends BubbleDocsIntegrator {

	private AssignReferenceCell localService;
	
	public AssignReferenceCellIntegrator(String _tokenUser, int _docId, String _cellId, String _reference) {
		localService = new AssignReferenceCell(_tokenUser, _docId, _cellId, _reference);
	}
	
	@Override
	public void execute() throws BubbleDocsException {
		localService.execute();
	}
	
	public int getResult(){
		return localService.getResult();
	}
}
