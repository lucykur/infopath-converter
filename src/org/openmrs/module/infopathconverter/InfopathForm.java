package org.openmrs.module.infopathconverter;

import org.openmrs.module.infopathconverter.rules.EncounterRule;
import org.openmrs.module.infopathconverter.rules.ObservationRule;
import org.openmrs.module.infopathconverter.rules.PatientRule;
import org.openmrs.module.infopathconverter.rules.Rule;
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
        applyRules(document, "patient", new PatientRule());
        applyRules(document, "encounter", new EncounterRule());
        applyRules(document, "obs", new ObservationRule());
    }

    private void applyRules(Document document, String bindingName, Rule rules) throws XPathExpressionException {
        String xpathQuery = String.format("//*[starts-with(@xd:binding,'%s/')]", bindingName);
        rules.apply(document, XPathUtils.matchNodes(document, xpathQuery));
    }
}
