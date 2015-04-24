package pt.tecnico.bubbledocs.service.remote;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.io.IOException;
import java.util.Map;

import javax.xml.registry.JAXRException;
import javax.xml.ws.BindingProvider;

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
import pt.ulisboa.tecnico.sdis.id.ws.SDId;
import pt.ulisboa.tecnico.sdis.id.ws.SDId_Service;
import pt.ulisboa.tecnico.sdis.id.ws.UserAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.UserDoesNotExist_Exception;

public class IDRemoteServices extends RemoteServices{
	
	private static final String UDDI_URL = "http://localhost:8081";
	
	private static final String ID_NAME = "SD-ID";
	
	
	public SDId getSpecificProxy(String uddiUrl,String name)
		throws RemoteInvocationException {
		String endpointAddress;
		try {
			endpointAddress = UDDILookup(uddiUrl,name);
		} catch (JAXRException e) {
			throw new RemoteInvocationException();
		}
		if (endpointAddress == null)
			throw new RemoteInvocationException();
		
		SDId_Service service = new SDId_Service();
		SDId id = service.getSDIdImplPort();
		
		BindingProvider bindingProvider = (BindingProvider) id;
		Map<String, Object> requestContext = bindingProvider.getRequestContext();
		requestContext.put(ENDPOINT_ADDRESS_PROPERTY, endpointAddress);
		return id;
	}
	
	public void createUser(String username, String email)
		throws InvalidUsernameException, DuplicateUsernameException,
		DuplicateEmailException, InvalidEmailException,
		RemoteInvocationException {
		
		SDId idRemote = getSpecificProxy(UDDI_URL, ID_NAME);
		try {
			idRemote.createUser(username, email);
		} catch (EmailAlreadyExists_Exception e) {
			throw new DuplicateEmailException();
		} catch (InvalidEmail_Exception e) {
			throw new InvalidEmailException();
		} catch (InvalidUser_Exception e) {
			throw new InvalidUsernameException();
		} catch (UserAlreadyExists_Exception e) {
			throw new DuplicateUsernameException(username);
		}
	}
	
	public void loginUser(String username, String password)
		throws LoginBubbleDocsException, RemoteInvocationException {
		
		SDId idRemote = getSpecificProxy(UDDI_URL, ID_NAME);
		try {
			idRemote.requestAuthentication(username, objectToBytes(password));
		} catch (AuthReqFailed_Exception | IOException e) {
			throw new LoginBubbleDocsException();
		}
	}
	
	public void removeUser(String username)
		throws LoginBubbleDocsException, RemoteInvocationException {
		
		SDId idRemote = getSpecificProxy(UDDI_URL, ID_NAME);
		try {
			idRemote.removeUser(username);
		} catch (UserDoesNotExist_Exception e) {
			throw new LoginBubbleDocsException();
		}
	}
	
	public void renewPassword(String username)
		throws LoginBubbleDocsException, RemoteInvocationException {
		
		SDId idRemote = getSpecificProxy(UDDI_URL, ID_NAME);
		try {
			idRemote.renewPassword(username);
		} catch (UserDoesNotExist_Exception e) {
			throw new LoginBubbleDocsException();
		}
	}
}
