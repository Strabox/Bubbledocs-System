package util.kerberos.messages;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import util.kerberos.exception.KerberosException;

public class KerberosMessage {

	protected final static String UTF8 = "UTF-8";
	
	/**
	 * @param document Document to validate.
	 * @param schemaPath XSD file path used to validate document.
	 * @throws KerberosException
	 */
	public static final void validateXMLDocument(Document document,String schemaPath) 
	throws KerberosException {
		
		File schemaFile = new File(schemaPath);
		Source schemaSource = new StreamSource(schemaFile);
		SchemaFactory schemaFactory;
		schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		try{
		    Schema schema = schemaFactory.newSchema(schemaSource);
		    Validator validator = schema.newValidator();
		    validator.validate(new DOMSource(document));
		}
		catch(SAXException a) {
			throw new KerberosException();
		}
		catch(IOException a) {
			throw new KerberosException();
		}
	}
}
