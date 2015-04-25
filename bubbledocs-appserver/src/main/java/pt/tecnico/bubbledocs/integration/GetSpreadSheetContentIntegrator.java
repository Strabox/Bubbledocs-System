package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.service.local.GetSpreadSheetContent;

public class GetSpreadSheetContentIntegrator extends BubbleDocsIntegrator {

	private GetSpreadSheetContent localService;
	
	public GetSpreadSheetContentIntegrator(String userToken, int docId) {
		localService = new GetSpreadSheetContent(userToken,docId);
	}
	
	@Override
	public void execute() throws BubbleDocsException {
		localService.execute();
	}
	
	public String[][] getResult(){
		return localService.getResult();
	}

}
