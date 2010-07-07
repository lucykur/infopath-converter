package org.openmrs.module.infopathconverter.rules.observation;

import org.openmrs.module.infopathconverter.rules.Rule;
import org.openmrs.module.infopathconverter.xmlutils.XmlDocumentFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
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
                if (concept.multiple != null) {
                    Node importedObservationNode = page.importNode(observationNode, true);
                    importedObservationNode.getAttributes().getNamedItem("conceptId").setNodeValue(concept.id);
                    createTransformedNodes(node, concept, importedObservationNode);
                }
            } else
                node.getParentNode().removeChild(node);

        }

    }

    private void createTransformedNodes(Node node, OpenMRSConcept concept, Node importedObservationNode) {
        ArrayList<Node> nodesWithAnswerConcepts = getNodesWithAnswerConcepts(node, importedObservationNode, concept);
        for (Node nodeWithAnswer : nodesWithAnswerConcepts) {
            node.getParentNode().appendChild(nodeWithAnswer);
        }
        node.getParentNode().removeChild(node);
    }

    private ArrayList<Node> getNodesWithAnswerConcepts(Node node, Node importedObservationNode, OpenMRSConcept concept) {
        ArrayList<Node> nodes = new ArrayList<Node>();
        if (concept.isNotMultiple()) {
            Node answerConcept = node.getAttributes().getNamedItem("xd:onValue");
            String answerConceptId;
            if (answerConcept != null) {
                answerConceptId = getConceptId(answerConcept.getNodeValue());
                importedObservationNode.getAttributes().getNamedItem("answerConceptId").setNodeValue(answerConceptId);
            } else {
                importedObservationNode.getAttributes().removeNamedItem("answerConceptId");
            }
            nodes.add(importedObservationNode);
            return nodes;
        } else {
            if (concept.answers != null) {
                for (String answer : concept.answers) {
                    Node nodeWithAnswer = importedObservationNode.cloneNode(true);
                    nodeWithAnswer.getAttributes().getNamedItem("answerConceptId").setNodeValue(answer);
                    nodes.add(nodeWithAnswer);
                }
                return nodes;
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
