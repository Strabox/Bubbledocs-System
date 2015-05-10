package pt.tecnico.bubbledocs.service.remote;

import pt.tecnico.bubbledocs.exceptions.DuplicateEmailException;
import pt.tecnico.bubbledocs.exceptions.DuplicateUsernameException;
import pt.tecnico.bubbledocs.exceptions.InvalidEmailException;
import pt.tecnico.bubbledocs.exceptions.InvalidUsernameException;
import pt.tecnico.bubbledocs.exceptions.LoginBubbleDocsException;
import pt.tecnico.bubbledocs.exceptions.RemoteInvocationException;
import pt.ulisboa.tecnico.sdis.id.ws.AuthReqFailed_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.EmailAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidEmail_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidUser_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.UserAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.UserDoesNotExist_Exception;
import sdis.cli.SdIdClient;

public class IDRemoteServices extends RemoteServices{
	
	private static final String ID_NAME = "SD-ID";
	
	public static byte[] credentials;
	
	private SdIdClient idClient;
	

	public void createUser(String username, String email)
		throws InvalidUsernameException, DuplicateUsernameException,
		DuplicateEmailException, InvalidEmailException,
		RemoteInvocationException {
		
		try {
			idClient = new SdIdClient(UDDI_URL, ID_NAME);
			idClient.createUser(username, email);
		} catch (EmailAlreadyExists_Exception e) {
			throw new DuplicateEmailException();
		} catch (InvalidEmail_Exception e) {
			throw new InvalidEmailException();
		} catch (InvalidUser_Exception e) {
			throw new InvalidUsernameException();
		} catch (UserAlreadyExists_Exception e) {
			throw new DuplicateUsernameException(username);
		} catch (Exception e){
			throw new RemoteInvocationException();
		}
	}
	
	public void loginUser(String username, String password)
		throws LoginBubbleDocsException, RemoteInvocationException {
		
		try {
			idClient = new SdIdClient(UDDI_URL, ID_NAME);
			credentials = idClient.requestAuthentication(username, password.getBytes());
		} catch (AuthReqFailed_Exception e) {
			throw new LoginBubbleDocsException();
		} catch (Exception e){
			throw new RemoteInvocationException();
		}
	}
	
	public void removeUser(String username)
		throws LoginBubbleDocsException, RemoteInvocationException {
		
		try {
			idClient = new SdIdClient(UDDI_URL, ID_NAME);
			idClient.removeUser(username);
		} catch (UserDoesNotExist_Exception e) {
			throw new LoginBubbleDocsException();
		} catch (Exception e){
			throw new RemoteInvocationException();
		}
	}
	
	public void renewPassword(String username)
		throws LoginBubbleDocsException, RemoteInvocationException {
		
		try {
			idClient = new SdIdClient(UDDI_URL, ID_NAME);
			idClient.renewPassword(username);
		} catch (UserDoesNotExist_Exception e) {
			throw new LoginBubbleDocsException();
		} catch (Exception e){
			throw new RemoteInvocationException();
		}
	}
}
