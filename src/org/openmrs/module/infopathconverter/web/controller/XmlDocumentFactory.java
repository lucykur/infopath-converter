package org.openmrs.module.infopathconverter.web.controller;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: lkurian
 * Date: Jun 29, 2010
 * Time: 2:47:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class XmlDocumentFactory {
    public static Document createXmlDocumentFromStream(InputStream stream) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder builder = createDocumentBuilder();
        return builder.parse(stream);
    }

    private static DocumentBuilder createDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setIgnoringComments(true);
        factory.setNamespaceAware(true);
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder;
    }

    public static Document createEmptyXmlDocument() throws ParserConfigurationException {
        return createDocumentBuilder().newDocument();
    }
}