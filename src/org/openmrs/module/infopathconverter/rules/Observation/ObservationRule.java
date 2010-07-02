package org.openmrs.module.infopathconverter.rules.Observation;

import org.openmrs.module.infopathconverter.rules.Rule;
import org.openmrs.module.infopathconverter.xmlutils.XmlDocumentFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.util.HashMap;

public class ObservationRule implements Rule {
    private HashMap<String, Element> observationExpressionMap;
    private Document document;


    public ObservationRule() throws Exception {
        document = XmlDocumentFactory.createEmptyXmlDocument();
        this.observationExpressionMap = new HashMap<String, Element>();

        observationExpressionMap.put("CWE", createObservationElement());
    }

    private Element createObservationElement() {
        Element codedObservation = document.createElement("obs");
        codedObservation.setAttribute("conceptId", "");
        return codedObservation;
    }


    public void apply(Document page, NodeList nodes, String observationCodedXsd) throws Exception {
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            openMRSConcept concept = transformAttribute(node, observationCodedXsd);
            Element observationNode = observationExpressionMap.get(concept.openmrs_datatype);
            if (observationNode != null) {
                Node importedObservationNode = page.importNode(observationNode, true);
                importedObservationNode.getAttributes().item(0).setNodeValue(concept.openmrs_concept);
                node.getParentNode().replaceChild(importedObservationNode, node);
            } else
                node.getParentNode().removeChild(node);
        }

    }


    private openMRSConcept transformAttribute(Node lookupNode, String observationCodedXsd) throws Exception {
        String bindingName = lookupNode.getAttributes().getNamedItem("xd:binding").getNodeValue();
        String[] bindingNameSegments = bindingName.split("/");
        return getOpenMRSConceptFromXSD(observationCodedXsd, bindingNameSegments);

    }

    private openMRSConcept getOpenMRSConceptFromXSD(String observationCodedXsd, String[] bindingNameSegments) throws Exception {
        for (int i = 1; i < bindingNameSegments.length; i++) {
            Document xsdStream = XmlDocumentFactory.createXmlDocumentFromStream(new ByteArrayInputStream(observationCodedXsd.getBytes()));
            NodeList openmrsConceptMatchList = xsdStream.getElementsByTagName(bindingNameSegments[i]);
            if (openmrsConceptMatchList.getLength() > 0) {
                Node node = openmrsConceptMatchList.item(0);
                if (node.getAttributes().getNamedItem("openmrs_concept") != null) {
                    String isMultiple = node.getAttributes().getNamedItem("multiple").getNodeValue();
                    String openmrs_concept = node.getAttributes().getNamedItem("openmrs_concept").getNodeValue();
                    String openmrs_conceptId = openmrs_concept.substring(0, openmrs_concept.indexOf('^'));
                    String openmrs_datatype = node.getAttributes().getNamedItem("openmrs_datatype").getNodeValue();
                    return new openMRSConcept(isMultiple, openmrs_conceptId, openmrs_datatype);
                }
            }

        }
        return null;
    }

}
