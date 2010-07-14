package org.openmrs.module.infopathconverter;

import org.openmrs.module.infopathconverter.rules.Rule;
import org.openmrs.module.infopathconverter.xmlutils.XPathUtil;
import org.openmrs.module.infopathconverter.xmlutils.XmlDocumentFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;

public class XmlDocument {
    private Document document;
    private HashMap<String, Rule> rules = new HashMap<String, Rule>();
    private XPathUtil xpath;

    public XmlDocument(String content) throws IOException, SAXException, ParserConfigurationException {
        document = XmlDocumentFactory.createXmlDocumentFromStream(new ByteArrayInputStream(content.getBytes()));

        xpath = new XPathUtil(document);
    }

    public XmlDocument() throws ParserConfigurationException {
        document = XmlDocumentFactory.createEmptyXmlDocument();

        xpath = new XPathUtil(document);
    }

    public Node getBody() {
        NodeList matchNodes = xpath.matchNodes("//body").getNodes();
        if (matchNodes.getLength() > 0) {
            return matchNodes.item(0);
        } else {
            return document.createElement("body");
        }
    }

    public void createPage(String title, Node body) {
        Element pageElement = document.createElement("page");
        pageElement.setAttribute("title", title);
        document.appendChild(pageElement);
        pageElement.appendChild(document.importNode(body, true));
    }

    public Document getDocument() {
        return document;
    }

    public void addRule(String binding, Rule rule) {
        rules.put(binding, rule);
    }

    public void applyRules() throws Exception {
        for (String binding : rules.keySet()) {
            Rule rule = rules.get(binding);
            rule.apply(xpath.matchNodes(binding));
        }
    }

    public Nodes match(String query) {
        return xpath.matchNodes(query);
    }
}
