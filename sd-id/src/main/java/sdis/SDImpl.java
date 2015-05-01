package sdis;

import java.security.Key;

import javax.jws.HandlerChain;
import javax.jws.WebService;

import pt.ulisboa.tecnico.sdis.id.ws.AuthReqFailed;
import pt.ulisboa.tecnico.sdis.id.ws.AuthReqFailed_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.EmailAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidEmail_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.InvalidUser_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.SDId; // classes generated from WSDL
import pt.ulisboa.tecnico.sdis.id.ws.UserAlreadyExists_Exception;
import pt.ulisboa.tecnico.sdis.id.ws.UserDoesNotExist;
import pt.ulisboa.tecnico.sdis.id.ws.UserDoesNotExist_Exception;
import sdis.domain.User;
import sdis.domain.UserManager;
import sdis.kerberos.KerberosManager;
import util.kerberos.Kerberos;
import util.kerberos.messages.KerberosRequest;
import util.kerberos.messages.KerberosTicket;

@WebService(
    endpointInterface="pt.ulisboa.tecnico.sdis.id.ws.SDId", 
    wsdlLocation="SD-ID.1_1.wsdl",
    name="SdId",
    portName="SDIdImplPort",
    targetNamespace="urn:pt:ulisboa:tecnico:sdis:id:ws",
    serviceName="SDId"
)
@HandlerChain(file="/handler-chain.xml")
public class SDImpl implements SDId {

	private final int TICKET_HOUR_DURATION = 2;
	
	private UserManager manager;
	
	@SuppressWarnings("unused")
	private KerberosManager kerberosManager;
	
	public SDImpl(){
		manager = new UserManager();
		kerberosManager = new KerberosManager();
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
	
	/* -----------------------WebService Methods---------------------------- */
	
	public void createUser(String userId, String emailAddress)
			throws EmailAlreadyExists_Exception, InvalidEmail_Exception,
			InvalidUser_Exception, UserAlreadyExists_Exception {
		
		User user = new User(userId,emailAddress);
		manager.addUser(user);
		//Requested by professor.
		System.out.println("First password for " + user.getUsername()+ " : " + user.getPassword());	
	
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
		if (!manager.usernameExists(userId)){
			UserDoesNotExist udne = new UserDoesNotExist();
			udne.setUserId(userId);
			throw new UserDoesNotExist_Exception("User não existe!!",udne);
		}
		else
			manager.removeUser(userId);
	}

	public byte[] requestAuthentication(String userId, byte[] reserved)
			throws AuthReqFailed_Exception {
		try{		
			if(userId == null || reserved == null){
				AuthReqFailed arf = new AuthReqFailed();
				throw new AuthReqFailed_Exception("Invalid request null detected", arf);
			}
			boolean userExists = manager.usernameExists(userId);
			if(userExists == true){
				KerberosRequest r = KerberosRequest.deserialize(reserved);
				
				Key kcs = Kerberos.generateSymKey(Kerberos.DES, 56);
				KerberosTicket ticket = new KerberosTicket(userId, r.getServer(),TICKET_HOUR_DURATION, kcs);
				return ticket.serialize(kcs);//TODO
			}
			else{
				AuthReqFailed arf = new AuthReqFailed();
				throw new AuthReqFailed_Exception("Invalid username or password!!", arf);
			}
		}catch(Exception e){
			AuthReqFailed arf = new AuthReqFailed();
			System.out.println(e);
			throw new AuthReqFailed_Exception("Problem authenticating!!", arf);
		}
	}

}
