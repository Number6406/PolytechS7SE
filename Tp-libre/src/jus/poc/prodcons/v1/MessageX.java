package jus.poc.prodcons.v1;


import com.sun.xml.internal.bind.api.Bridge;
import com.sun.xml.internal.ws.api.message.Message;
import com.sun.xml.internal.ws.api.message.MessageHeaders;
import com.sun.xml.internal.ws.spi.db.XMLBridge;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Source;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author bonhourg
 */
public class MessageX extends Message {

    @Override
    public boolean hasHeaders() {
    }

    @Override
    public MessageHeaders getHeaders() {
    }

    @Override
    public String getPayloadLocalPart() {
    }

    @Override
    public String getPayloadNamespaceURI() {
    }

    @Override
    public boolean hasPayload() {
    }

    @Override
    public Source readEnvelopeAsSource() {
    }

    @Override
    public Source readPayloadAsSource() {
    }

    @Override
    public SOAPMessage readAsSOAPMessage() throws SOAPException {
    }

    @Override
    public <T> T readPayloadAsJAXB(Unmarshaller u) throws JAXBException {
    }

    @Override
    public <T> T readPayloadAsJAXB(Bridge<T> bridge) throws JAXBException {
    }

    @Override
    public <T> T readPayloadAsJAXB(XMLBridge<T> xmlb) throws JAXBException {
    }

    @Override
    public XMLStreamReader readPayload() throws XMLStreamException {
    }

    @Override
    public void writePayloadTo(XMLStreamWriter writer) throws XMLStreamException {
    }

    @Override
    public void writeTo(XMLStreamWriter writer) throws XMLStreamException {
    }

    @Override
    public void writeTo(ContentHandler ch, ErrorHandler eh) throws SAXException {
    }

    @Override
    public Message copy() {
    }
    
}
