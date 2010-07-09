package org.openmrs.module.infopathconverter.xmlutils;

import org.openmrs.module.infopathconverter.rules.Nodes;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;


public class XPathUtils {
    public static Nodes matchNodes(Document document, String query)  {
        XPathFactory factory = XPathFactory.newInstance();
        javax.xml.xpath.XPath xpath = factory.newXPath();
        xpath.setNamespaceContext(new XDNamespaceContext());
        try {
            return new Nodes((NodeList) xpath.evaluate(query, document,
                    XPathConstants.NODESET));
        } catch (XPathExpressionException e) {
            throw new RuntimeException(String.format("Could not find anyting matching for %s",query),e);
        }
    }

}