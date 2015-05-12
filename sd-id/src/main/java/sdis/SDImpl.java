package sdis;

import java.security.Key;
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
import util.kerberos.exception.KerberosException;
import util.kerberos.messages.KerberosReply;
import util.kerberos.messages.KerberosRequest;
import util.kerberos.messages.KerberosServerAuthentication;
import util.kerberos.messages.KerberosTicket;

@WebService(
    endpointInterface="pt.ulisboa.tecnico.sdis.id.ws.SDId", 
    wsdlLocation="SD-ID.1_1.wsdl",
    name="SdId",
    portName="SDIdImplPort",
    targetNamespace="urn:pt:ulisboa:tecnico:sdis:id:ws",
    serviceName="SDId"
)
public class SDImpl implements SDId {
	
	/**
	 * Used to control business logic.
	 */
	private UserManager userManager;
	
	/**
	 * Used to manage Kerberos Protocol
	 */
	private KerberosManager kerberosManager;
	
	
	public SDImpl() throws Exception{
		userManager = new UserManager();
		kerberosManager = new KerberosManager();
		populateServer();
	}

	/* populateServer() - Used to populate users in the server launch. */
	private void populateServer(){
		User user;
		try{
			user = new User("alice","alice@tecnico.pt");
			user.setPassword("Aaa1");
			userManager.addUser(user);
			user = new User("bruno","bruno@tecnico.pt");
			user.setPassword("Bbb2");
			userManager.addUser(user);
			user = new User("carla","carla@tecnico.pt");
			user.setPassword("Ccc3");
			userManager.addUser(user);
			user = new User("duarte","duarte@tecnico.pt");
			user.setPassword("Ddd4");
			userManager.addUser(user);
			user = new User("eduardo","eduardo@tecnico.pt");
			user.setPassword("Eee5");
			userManager.addUser(user);
			user = new User("root","root@tecnico.pt");
			user.setPassword("root");
			userManager.addUser(user);
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
	/* ========================= WebService Methods ============================ */
	
	public void createUser(String userId, String emailAddress)
			throws EmailAlreadyExists_Exception, InvalidEmail_Exception,
			InvalidUser_Exception, UserAlreadyExists_Exception {
		
		User user = new User(userId,emailAddress);
		userManager.addUser(user);
		//Requested by professor.
		System.out.println("First password for " + user.getUsername()+ " : " + user.getPassword());	
	}
	
	
	public void renewPassword(String userId) throws UserDoesNotExist_Exception {
		if(!userManager.usernameExists(userId)){
			UserDoesNotExist udne = new UserDoesNotExist();
			udne.setUserId(userId);
			throw new UserDoesNotExist_Exception(userId, udne);
		}
		String pass = userManager.getUserByUsername(userId).setNewPassword();
		System.out.println("Password for "+ userId+ ": " + pass);
	}

	
	public void removeUser(String userId) throws UserDoesNotExist_Exception {
		if (!userManager.usernameExists(userId)){
			UserDoesNotExist udne = new UserDoesNotExist();
			udne.setUserId(userId);
			throw new UserDoesNotExist_Exception("User does not exist!!",udne);
		}
		else
			userManager.removeUser(userId);
	}

	
	public byte[] requestAuthentication(String userId, byte[] reserved)
		throws AuthReqFailed_Exception {
		try{		
		if(userId != null && reserved != null) {
			boolean userExists = userManager.usernameExists(userId);
			if(userExists){
				KerberosRequest r = KerberosRequest.deserialize(reserved);
				if(!kerberosManager.nonceIsValid(r.getNonce()) && 
					kerberosManager.getServerKey(r.getServer()) != null){
					
					Key ks = kerberosManager.getServerKey(r.getServer());
					Key kc = Kerberos.getKeyFromBytes(
					Kerberos.digestPassword(userManager.getUserPassword(userId).getBytes(), Kerberos.MD5));
					Key kcs = Kerberos.generateKerberosKey();
					KerberosTicket ticket = new KerberosTicket(userId, r.getServer(),
							KerberosManager.TICKET_HOUR_DURATION, kcs);
					KerberosServerAuthentication ksa = new KerberosServerAuthentication(kcs, r.getNonce());
					KerberosReply rep = new KerberosReply(ticket.serialize(ks), ksa.serialize(kc));
					kerberosManager.addNonce(r.getNonce(),ticket.getEndTime());
					return rep.serialize();
				}
			}
		}
		AuthReqFailed arf = new AuthReqFailed();
		throw new AuthReqFailed_Exception("Invalid Request!!", arf);
		}catch(KerberosException e){
			AuthReqFailed arf = new AuthReqFailed();
			throw new AuthReqFailed_Exception("Problem authenticating!!", arf);
		}
	}

}
