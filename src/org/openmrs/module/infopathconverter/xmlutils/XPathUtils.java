package org.openmrs.module.infopathconverter.xmlutils;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;


public class XPathUtils {
    public static NodeList matchNodes(Document document, String query) throws XPathExpressionException {
        XPathFactory factory = XPathFactory.newInstance();
        javax.xml.xpath.XPath xpath = factory.newXPath();
        xpath.setNamespaceContext(new XDNamespaceContext());
        return (NodeList) xpath.evaluate(query, document,
                XPathConstants.NODESET);
    }

}