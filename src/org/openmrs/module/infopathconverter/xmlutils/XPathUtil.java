package org.openmrs.module.infopathconverter.xmlutils;

import org.openmrs.module.infopathconverter.Nodes;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;


public class XPathUtil {
    private Document document;
    private XPath xpath;

    public XPathUtil(Document document) {
        this.document = document;
        XPathFactory factory = XPathFactory.newInstance();
        xpath = factory.newXPath();
        xpath.setNamespaceContext(new XDNamespaceContext());

    }

    public Nodes matchNodes(String query) {
        try {
            return new Nodes((NodeList) xpath.evaluate(query, document,
                    XPathConstants.NODESET));
        } catch (XPathExpressionException e) {
            throw new RuntimeException(String.format("Could not find anyting matching for %s", query), e);
        }
    }

}