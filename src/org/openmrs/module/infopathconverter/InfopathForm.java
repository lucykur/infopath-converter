package org.openmrs.module.infopathconverter;

import org.openmrs.module.infopathconverter.rules.PatientRule;
import org.openmrs.module.infopathconverter.rules.Rule;
import org.openmrs.module.infopathconverter.rules.SubmitButtonRule;
import org.openmrs.module.infopathconverter.rules.encounter.EncounterLocationRule;
import org.openmrs.module.infopathconverter.rules.encounter.EncounterRule;
import org.openmrs.module.infopathconverter.rules.observation.InfopathXsd;
import org.openmrs.module.infopathconverter.rules.observation.ObservationRule;
import org.openmrs.module.infopathconverter.rules.observation.TemplateXml;
import org.openmrs.module.infopathconverter.xmlutils.XPathUtils;
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


public class InfopathForm {
    private String formName;
    private String content;

    public InfopathForm(String formName, String content) {
        this.formName = formName;
        this.content = content;

    }

    public Document toPage(TemplateXml templateXml, InfopathXsd xsd) throws Exception {


        XmlDocument page = new XmlDocument();
        page.createPage(formName, new XmlDocument(content).getBody());

        page.addRule("//*[starts-with(@xd:binding,'patient/')]", new PatientRule(page.getDocument()));
        page.addRule("//*[starts-with(@xd:binding,'encounter/encounter.encounter_datetime') or contains(@xd:binding,'encounter/encounter.provider_id')]", new EncounterRule(page.getDocument()));
        page.addRule("//*[starts-with(@xd:binding,'encounter/encounter.location_id')]", new EncounterLocationRule(page.getDocument()));
        page.addRule("//*[starts-with(@xd:binding,'obs/')]", new ObservationRule(page.getDocument(), templateXml, xsd));
        page.addRule("//input[@xd:CtrlId='SubmitButton']", new SubmitButtonRule(page.getDocument()));

        page.applyRules();

        return page.getDocument();
    }


    private class XmlDocument {
        private Document document;
        private HashMap<String, Rule> rules = new HashMap<String, Rule>();

        public XmlDocument(String content) throws IOException, SAXException, ParserConfigurationException {
            document = XmlDocumentFactory.createXmlDocumentFromStream(new ByteArrayInputStream(content.getBytes()));

        }

        public XmlDocument() throws ParserConfigurationException {
            document = XmlDocumentFactory.createEmptyXmlDocument();

        }

        public Node getBody() {
            NodeList matchNodes = XPathUtils.matchNodes(document, "//body").getNodes();
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
                rule.apply(XPathUtils.matchNodes(document, binding));
            }
        }
    }
}
