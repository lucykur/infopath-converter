package org.openmrs.module.infopathconverter;

import org.openmrs.module.infopathconverter.rules.EncounterRule;
import org.openmrs.module.infopathconverter.rules.Observation.ObservationRule;
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
    private String observationCodedXml;

    public InfopathForm(String formName, String content) {
        this.formName = formName;
        this.content = content;
        this.observationCodedXml = null;

    }

    public InfopathForm(String formName, String content, String observationCodedXml) {
        this.formName = formName;
        this.content = content;
        this.observationCodedXml = observationCodedXml;
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


    private void extractBindings(Document document) throws Exception {
        applyRules(document, "//*[starts-with(@xd:binding,'patient/')]", new PatientRule());
        applyRules(document, "//*[starts-with(@xd:binding,'encounter/') or contains(@xd:binding,'encounter/encounter.provider_id')]", new EncounterRule());
        applyRules(document, "//*[starts-with(@xd:binding,'obs/')]", new ObservationRule());
    }

    private void applyRules(Document document, String query, Rule rules) throws Exception {
        rules.apply(document, XPathUtils.matchNodes(document, query), observationCodedXml);
    }
}
