package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.service.local.AssignBinaryFunctionToCell;

public class AssignBinaryFunctionToCellIntegrator extends BubbleDocsIntegrator {

	private AssignBinaryFunctionToCell localService;
	
	public AssignBinaryFunctionToCellIntegrator(String userToken, int docId, String cellid, String expression) {
		localService = new AssignBinaryFunctionToCell(userToken,docId,cellid,expression);
	}
	
	@Override
	public void execute() throws BubbleDocsException {
		localService.execute();
	}

	public int getResult(){
		return localService.getResult();
	}
	
}
