package pt.tecnico.bubbledocs.integration;

import pt.tecnico.bubbledocs.exceptions.BubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.tecnico.bubbledocs.exceptions.UnavailableServiceException;
import pt.tecnico.bubbledocs.service.local.GetUsername4TokenService;
import pt.tecnico.bubbledocs.service.local.RenewPassword;
import pt.tecnico.bubbledocs.service.remote.IDRemoteServices;

public class RenewPasswordIntegrator extends BubbleDocsIntegrator {

	private RenewPassword localService;
	private IDRemoteServices remoteServer;
	private String token;
	
	public RenewPasswordIntegrator(String userToken) {
		this.token = userToken;
		localService = new RenewPassword(token);
		remoteServer = new IDRemoteServices();
	}
	
	
	@Override
	public void execute() throws BubbleDocsException {
		localService.execute();
		GetUsername4TokenService user4tok = new GetUsername4TokenService(token);
		user4tok.execute();
		try{
			remoteServer.renewPassword(user4tok.getUsername());
		}
		catch(RemoteInvocationException e){
			throw new UnavailableServiceException();
		}
	}
}
