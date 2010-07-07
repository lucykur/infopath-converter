package org.openmrs.module.infopathconverter.rules.observation;

import org.openmrs.module.infopathconverter.rules.Rule;
import org.openmrs.module.infopathconverter.xmlutils.XmlDocumentFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;

public class ObservationRule implements Rule {
    private HashMap<String, Element> expressions;
    private Document document;
    private OpenMRSConceptFinder finder;
    private String template;


    public ObservationRule(String template) throws Exception {
        this.template = template;
        document = XmlDocumentFactory.createEmptyXmlDocument();
        this.expressions = new HashMap<String, Element>();
        expressions.put("CWE", createObservationElement());

    }

    private Element createObservationElement() {
        Element codedObservation = document.createElement("obs");
        codedObservation.setAttribute("conceptId", "");
        codedObservation.setAttribute("answerConceptId", "");
        return codedObservation;
    }


    public void apply(Document page, NodeList nodes) throws Exception {
        finder = new OpenMRSConceptFinder(template);
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            OpenMRSConcept concept = finder.findConcept(node);
            Element observationNode = expressions.get(concept.datatype);
            if (observationNode != null) {
                if (concept.isMultiple()) {
                    Node importedObservationNode = page.importNode(observationNode, true);
                    importedObservationNode.getAttributes().getNamedItem("conceptId").setNodeValue(concept.id);
                    setAnswerConcepts(node, importedObservationNode);
                    node.getParentNode().replaceChild(importedObservationNode, node);
                }
            } else
                node.getParentNode().removeChild(node);

        }

    }

    private void setAnswerConcepts(Node node, Node importedObservationNode) {
        Node answerConcept = node.getAttributes().getNamedItem("xd:onValue");
        String answerConceptId;
        if (answerConcept != null) {
            answerConceptId = getConceptId(answerConcept.getNodeValue());
            importedObservationNode.getAttributes().getNamedItem("answerConceptId").setNodeValue(answerConceptId);
        } else {
            importedObservationNode.getAttributes().removeNamedItem("answerConceptId");
        }

    }


    private String getConceptId(String openmrs_concept) {
        int index = openmrs_concept.indexOf('^');
        if (index != -1)
            return openmrs_concept.substring(0, index);
        return null;
    }

}
