package pt.tecnico.bubbledocs;



public class BubbleApplication {
	
    /* main - Bubbledocs main function. */
	public static void main(String[] args){
		System.out.println("Bem-Vindos ao BubbleDocs!!!");		
		/* This test with a true servers. 
		try{
			LoginUserIntegrator lu = new LoginUserIntegrator("root", "root");
			lu.execute();
			String token = lu.getUserToken();
			LoginUserIntegrator la = new LoginUserIntegrator("andre", "F81DEYZK");
			la.execute();
			CreateSpreadSheetIntegrator csi = new CreateSpreadSheetIntegrator(la.getUserToken(), "a", 10, 10);
			csi.execute();
			ExportDocumentIntegrator e = new ExportDocumentIntegrator(la.getUserToken(), 0);
			e.execute();
		}catch(Exception e){
			e.printStackTrace();
			System.out.println(e);
		}*/
		
	}

}
