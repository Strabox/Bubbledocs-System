package util.kerberos.messages;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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
	 * Validate XMLDocument with XSD schema given.
	 * @param document Document to validate.
	 * @param schemaPath XSD file path used to validate document.
	 * @throws KerberosException
	 */
	protected static final void validateXMLDocument(Document document,String schemaPath) 
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
	
	/**
	 * Convert XML in bytes to Document object.
	 * @param docBytes
	 * @return
	 * @throws KerberosException
	 */
	protected static final Document getXMLDocumentFromBytes(byte[] docBytes)
	throws KerberosException {
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
			Document document;
			document = builder.parse(new ByteArrayInputStream(docBytes));
			return document;
		}catch(ParserConfigurationException e){
			throw new KerberosException();
		} catch (SAXException e) {
			throw new KerberosException();
		} catch (IOException e) {
			throw new KerberosException();
		}
	}
	
}
