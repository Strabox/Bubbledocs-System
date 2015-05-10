package pt.tecnico.bubbledocs.integration;


import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.service.local.AssignLiteralCell;

public class AssignLiteralCellIntegrator extends BubbleDocsIntegrator {

	private AssignLiteralCell localService;
	
	public AssignLiteralCellIntegrator(String _tokenUser, int _docId, String _cellId, String _literal) {
		localService = new AssignLiteralCell(_tokenUser, _docId, _cellId, _literal);
	}
	
	@Override
	public void execute() throws BubbleDocsException {
		localService.execute();
	}
	
	public int getResult(){
		return localService.getResult();
	}
}