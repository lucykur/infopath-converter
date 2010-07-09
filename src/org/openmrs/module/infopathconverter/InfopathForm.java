package org.openmrs.module.infopathconverter;

import org.openmrs.module.infopathconverter.rules.*;
import org.openmrs.module.infopathconverter.rules.encounter.EncounterLocationRule;
import org.openmrs.module.infopathconverter.rules.encounter.EncounterRule;
import org.openmrs.module.infopathconverter.rules.observation.ObservationRule;
import org.openmrs.module.infopathconverter.rules.observation.TemplateXml;
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

    public Document toPage(TemplateXml templateXml) throws Exception {
        ByteArrayInputStream stream = new ByteArrayInputStream(content.getBytes());
        Document rawDocument = XmlDocumentFactory.createXmlDocumentFromStream(stream);
        Node node = extractPageBody(rawDocument);

        Document page = XmlDocumentFactory.createEmptyXmlDocument();
        Element pageElement = page.createElement("page");
        pageElement.setAttribute("title", formName);
        page.appendChild(pageElement);
        pageElement.appendChild(page.importNode(node, true));

        extractBindings(page, templateXml);
        return page;
    }

    public Node extractPageBody(Document document) throws XPathExpressionException {
        NodeList matchNodes = XPathUtils.matchNodes(document, "//body").getNodes();
        if (matchNodes.getLength() > 0) {
            return matchNodes.item(0);
        } else {
            return document.createElement("body");
        }
    }


    private void extractBindings(Document document, TemplateXml templateXml) throws Exception {
        applyRules(document, "//*[starts-with(@xd:binding,'patient/')]", new PatientRule(document));
        applyRules(document, "//*[starts-with(@xd:binding,'encounter/encounter.encounter_datetime') or contains(@xd:binding,'encounter/encounter.provider_id')]", new EncounterRule(document));
        applyRules(document, "//*[starts-with(@xd:binding,'encounter/encounter.location_id')]", new EncounterLocationRule(document));
        applyRules(document, "//*[starts-with(@xd:binding,'obs/')]", new ObservationRule(document,templateXml));
    }

    private void applyRules(Document document, String query, Rule rule) throws Exception {
        rule.apply(XPathUtils.matchNodes(document, query));
    }
}
