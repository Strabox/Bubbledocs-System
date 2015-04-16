package sdis;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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
		populateServer();
	}

	/* populateServer() - Used to populate users in the server launch. */
	private void populateServer(){
		User user;
		try{
			user = new User("alice","alice@tecnico.pt");
			user.setPassword("Aaa1");
			manager.addUser(user);
			user = new User("bruno","bruno@tecnico.pt");
			user.setPassword("Bbb2");
			manager.addUser(user);
			user = new User("carla","carla@tecnico.pt");
			user.setPassword("Ccc3");
			manager.addUser(user);
			user = new User("duarte","duarte@tecnico.pt");
			user.setPassword("Ddd4");
			manager.addUser(user);
			user = new User("eduardo","eduardo@tecnico.pt");
			user.setPassword("Eee5");
			manager.addUser(user);
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
	/*
	 * bytesToObject(bytes) - Transforms bytes in an object.
	 */
	private Object bytesToObject(byte[] bytes) throws IOException,
		ClassNotFoundException {
		ByteArrayInputStream b = new ByteArrayInputStream(bytes);
		ObjectInputStream o = new ObjectInputStream(b);
		return o.readObject();
	}
	
	/* -----------------------WebService Methods---------------------------- */
	
	public void createUser(String userId, String emailAddress)
			throws EmailAlreadyExists_Exception, InvalidEmail_Exception,
			InvalidUser_Exception, UserAlreadyExists_Exception {
		User user = new User(userId,emailAddress);
		manager.addUser(user);
		System.out.println(user.getPassword());	//Requested by professor.
	}

	public void renewPassword(String userId) throws UserDoesNotExist_Exception {
		if(!manager.usernameExists(userId)){
			UserDoesNotExist udne = new UserDoesNotExist();
			udne.setUserId(userId);
			throw new UserDoesNotExist_Exception(userId, udne);
		}
		String pass = manager.getUserByUsername(userId).setNewPassword();
		System.out.println("Password for "+ userId+ ": " + pass);
	}

	public void removeUser(String userId) throws UserDoesNotExist_Exception {
		manager.removeUser(userId);
	}

	public byte[] requestAuthentication(String userId, byte[] reserved)
			throws AuthReqFailed_Exception {
		try{		
			if(userId == null | reserved == null){
				AuthReqFailed arf = new AuthReqFailed();
				throw new AuthReqFailed_Exception("Invalid username or password!!", arf);
			}
			String password = (String) bytesToObject(reserved);
			boolean loggedin = manager.verifyUserPassword(userId, password);
			if(loggedin == true){
				byte[] res = new byte[1];
				res[0] = (byte) 1;
				return res;
			}
			else{
				AuthReqFailed arf = new AuthReqFailed();
				throw new AuthReqFailed_Exception("Invalid username or password!!", arf);
			}
		}catch(Exception e){
			AuthReqFailed arf = new AuthReqFailed();
			throw new AuthReqFailed_Exception("Problem authenticating!!", arf);
		}
	}

}
