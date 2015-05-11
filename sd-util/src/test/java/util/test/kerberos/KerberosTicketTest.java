package util.test.kerberos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.security.Key;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import util.kerberos.Kerberos;
import util.kerberos.exception.KerberosException;
import util.kerberos.messages.KerberosTicket;


public class KerberosTicketTest {

	
	private static final String CLIENT = "Andre24";
	
	private static final String SERVER = "server1";
	
	private static final int DURATION = 1;
	
	private static Key KS;
	
	private static Key KCS;
	
	private static KerberosTicket tick;
 	
	@Before
	public void createKeys() throws KerberosException{
		KS = Kerberos.generateKerberosKey();
		KCS = Kerberos.generateKerberosKey();
		KerberosTicket t = new KerberosTicket(CLIENT, SERVER, DURATION, KCS);
		tick = KerberosTicket.deserialize(t.serialize(KS), KS);
	}
	
	
	@Test
	public void createTicketSuccess() throws KerberosException{
		KerberosTicket ticket = new KerberosTicket(CLIENT, SERVER, DURATION, KCS);
		byte[] ticketByte = ticket.serialize(KS);
		assertNotNull(ticketByte);
		KerberosTicket ticket2 = KerberosTicket.deserialize(ticketByte, KS);
		assertNotNull(ticket2);
		assertEquals(ticket2.getClient(),ticket.getClient());
		assertEquals(ticket2.getServer(),ticket.getServer());
		assertNotNull(ticket2.getBeginTime());
		assertNotNull(ticket2.getEndTime());
		assertEquals(ticket.getKcs().getFormat(), ticket2.getKcs().getFormat());
	}
	
	@Test(expected = KerberosException.class)
	public void serializeNull() throws KerberosException{
		tick.serialize(null);
	}
	
	@Test
	public void validTicket(){
		tick.isValidTicket(SERVER);
	}
	
	@Test
	public void invalidServer(){
		assertFalse(tick.isValidTicket("wrongID"));
	}

	@Test
	public void expiredTicket(){
		tick.setEndTime(new Date());
		assertFalse(tick.isValidTicket(SERVER));
	}
	
	
}
