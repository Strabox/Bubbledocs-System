package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.service.local.CreateSpreadSheet;

public class CreateSpreadSheetIntegrator extends BubbleDocsIntegrator {

	private CreateSpreadSheet localService;
	
	public CreateSpreadSheetIntegrator(String userToken, String name, int lines, int columns) {
		localService = new CreateSpreadSheet(userToken, name, lines, columns);
	}
	
	@Override
	public void execute() throws BubbleDocsException {
		localService.execute();
	}
	
}
