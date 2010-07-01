package org.openmrs.module.infopathconverter;

import org.openmrs.module.infopathconverter.rules.PatientRules;
import org.openmrs.module.infopathconverter.xmlutils.XPathUtils;
import org.openmrs.module.infopathconverter.xmlutils.XmlDocumentFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathExpressionException;
import java.io.ByteArrayInputStream;


public class InfopathForm {
    private String formName;
    private String content;

    public InfopathForm(String formName, String content) {
        this.formName = formName;
        this.content = content;
    }

    public Document toPage() throws Exception {

        ByteArrayInputStream stream = new ByteArrayInputStream(content.getBytes());
        Document rawDocument = XmlDocumentFactory.createXmlDocumentFromStream(stream);
        Node node = extractPageBody(rawDocument);


        Document page = XmlDocumentFactory.createEmptyXmlDocument();
        Element pageElement = page.createElement("page");
        pageElement.setAttribute("title", formName);
        page.appendChild(pageElement);
        pageElement.appendChild(page.importNode(node, true));

        extractBindings(page);
        return page;
    }

    public Node extractPageBody(Document document) throws XPathExpressionException {
        NodeList matchNodes = XPathUtils.matchNodes(document, "//body");
        if (matchNodes.getLength() > 0) {
            return matchNodes.item(0);
        } else {
            return document.createElement("body");
        }
    }


    private void extractBindings(Document document) throws XPathExpressionException {
        NodeList nodes = XPathUtils.matchNodes(document, "//*[starts-with(@xd:binding,'patient/')]");
        new PatientRules().apply(document, nodes);

    }
}
