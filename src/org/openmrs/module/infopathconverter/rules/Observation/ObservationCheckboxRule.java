package org.openmrs.module.infopathconverter.rules.Observation;

import org.openmrs.module.infopathconverter.rules.Rule;
import org.openmrs.module.infopathconverter.xmlutils.XmlDocumentFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.util.HashMap;

public class ObservationCheckboxRule implements Rule {
    private HashMap<String, Element> observationExpressionMap;
    private Document document;
    private String template;


    public ObservationCheckboxRule(String template) throws Exception {
        this.template = template;
        document = XmlDocumentFactory.createEmptyXmlDocument();
        this.observationExpressionMap = new HashMap<String, Element>();
        observationExpressionMap.put("CWE", createObservationElement());
    }

    private Element createObservationElement() {
        Element codedObservation = document.createElement("obs");
        codedObservation.setAttribute("conceptId", "");
        codedObservation.setAttribute("answerConceptId", "");
        return codedObservation;
    }


    public void apply(Document page, NodeList nodes) throws Exception {
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            openMRSConcept concept = transformAttribute(node, template);
            Element observationNode = observationExpressionMap.get(concept.openmrs_datatype);
            if (observationNode != null) {
                Node importedObservationNode = page.importNode(observationNode, true);
                importedObservationNode.getAttributes().getNamedItem("conceptId").setNodeValue(concept.openmrs_concept);
                String answerConceptId = getConceptId(node.getAttributes().getNamedItem("xd:onValue").getNodeValue());
                if (answerConceptId != null)
                    importedObservationNode.getAttributes().getNamedItem("answerConceptId").setNodeValue(answerConceptId);
                node.getParentNode().replaceChild(importedObservationNode, node);
            } else
                node.getParentNode().removeChild(node);
        }

    }


    private openMRSConcept transformAttribute(Node lookupNode, String observationCodedXml) throws Exception {
        String bindingName = lookupNode.getAttributes().getNamedItem("xd:binding").getNodeValue();
        String[] bindingNameSegments = bindingName.split("/");
        return getOpenMRSConceptFromXML(observationCodedXml, bindingNameSegments);

    }

    private openMRSConcept getOpenMRSConceptFromXML(String templateXml, String[] bindingNameSegments) throws Exception {
        for (int i = 1; i < bindingNameSegments.length; i++) {
            Document xmlStream = XmlDocumentFactory.createXmlDocumentFromStream(new ByteArrayInputStream(templateXml.getBytes()));
            NodeList openmrsConceptMatchList = xmlStream.getElementsByTagName(bindingNameSegments[i]);
            if (openmrsConceptMatchList.getLength() > 0) {
                Node node = openmrsConceptMatchList.item(0);
                if (node.getAttributes().getNamedItem("openmrs_concept") != null) {

                    Node isMultipleNode = node.getAttributes().getNamedItem("multiple");
                    String isMultiple = "0";
                    if (isMultipleNode != null) {
                        isMultiple = isMultipleNode.getNodeValue();
                    }

                    String openmrs_concept = node.getAttributes().getNamedItem("openmrs_concept").getNodeValue();

                    String openmrs_datatype = node.getAttributes().getNamedItem("openmrs_datatype").getNodeValue();

                    return new openMRSConcept(isMultiple, getConceptId(openmrs_concept), openmrs_datatype);
                }
            }

        }
        return null;
    }

    private String getConceptId(String openmrs_concept) {
        int index = openmrs_concept.indexOf('^');
        if (index != -1)
            return openmrs_concept.substring(0, index);
        return null;
    }

}
