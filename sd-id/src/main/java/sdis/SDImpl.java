package sdis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.jws.*;

import pt.ulisboa.tecnico.sdis.id.ws.*; // classes generated from WSDL
import sdis.domain.User;
import sdis.domain.UserManager;

@WebService(
    endpointInterface="pt.ulisboa.tecnico.sdis.id.ws.SDId", 
    wsdlLocation="SD-ID.1_1.wsdl",
    name="SdId",
    portName="SDIdImplPort",
    targetNamespace="urn:pt:ulisboa:tecnico:sdis:id:ws",
    serviceName="SDId"
)
public class SDImpl implements SDId {

	private UserManager manager;
	
	
	public SDImpl(){
		manager = new UserManager();
	}

	public void createUser(String userId, String emailAddress)
			throws EmailAlreadyExists_Exception, InvalidEmail_Exception,
			InvalidUser_Exception, UserAlreadyExists_Exception {
		User user = new User(userId,emailAddress);
		manager.addUser(user);
	}

	public void renewPassword(String userId) throws UserDoesNotExist_Exception {
		if(!manager.usernameExists(userId)){
			UserDoesNotExist udne = new UserDoesNotExist();
			udne.setUserId(userId);
			throw new UserDoesNotExist_Exception(userId, udne);
		}
		User user = manager.getUserByUsername(userId);
		String pass = user.setNewPassword();
	}

	public void removeUser(String userId) throws UserDoesNotExist_Exception {
		// TODO Auto-generated method stub
		
	}

	public byte[] requestAuthentication(String userId, byte[] reserved)
			throws AuthReqFailed_Exception {
		try{
			ByteArrayInputStream bIn = new ByteArrayInputStream(reserved);
			ObjectInputStream oIn = new ObjectInputStream(bIn);
			String password = (String) oIn.readObject();
			
			boolean loggedin = manager.verifyUserPassword(userId, password);
			
			ByteArrayOutputStream bOut = new ByteArrayOutputStream();
			ObjectOutputStream oOut = new ObjectOutputStream(bOut);
			oOut.writeObject(loggedin);
			return bOut.toByteArray(); 
		}
		catch (IOException e){
			AuthReqFailed a = new AuthReqFailed();
			throw new AuthReqFailed_Exception("Authentication failed!", a);
		}
		catch(ClassNotFoundException e ){
			AuthReqFailed a = new AuthReqFailed();
			throw new AuthReqFailed_Exception("Authentication failed!", a);
		}
	}

}
