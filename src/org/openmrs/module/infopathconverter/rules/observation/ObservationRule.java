package org.openmrs.module.infopathconverter.rules.observation;

import org.openmrs.module.infopathconverter.rules.NodeAction;
import org.openmrs.module.infopathconverter.rules.Nodes;
import org.openmrs.module.infopathconverter.rules.Rule;
import org.openmrs.module.infopathconverter.rules.XmlNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ObservationRule extends Rule {
    private HashMap<String, Element> expressions;
    private TemplateXml templateXml;


    public ObservationRule(Document document, TemplateXml templateXml) throws Exception {
        super(document);
        addExpression("CWE", createObservationNode());
        this.templateXml = templateXml;
    }


    private XmlNode createObservationNode() {
        Element element = document.createElement("obs");
        element.setAttribute("conceptId", "");
        element.setAttribute("answerConceptId", "");
        return new XmlNode(element);
    }


    public void apply(Nodes nodes) throws Exception {
        nodes.forEach(new NodeAction() {
            public void execute(XmlNode node) throws Exception {
                XmlNode concept = templateXml.findConcept(node);
                XmlNode observation = getExpressionAsNode(concept.getDataType());
                if (observation != null) {
                    if (concept.hasMultiple()) {
                        observation.setConceptId(concept.getConceptId());
                        createTransformedNodes(node, concept, observation);
                    }
                } else
                    node.remove();

            }
        });

    }

    private void createTransformedNodes(XmlNode node, XmlNode concept, XmlNode importedObservationNode) {
        ArrayList<XmlNode> answers = getNodesWithAnswerConcepts(node, importedObservationNode, concept);
        for (XmlNode answer : answers) {
            node.appendChild(answer);
        }
        node.remove();
    }

    private ArrayList<XmlNode> getNodesWithAnswerConcepts(XmlNode node, XmlNode importedObservationNode, XmlNode concept) {
        ArrayList<XmlNode> nodes = new ArrayList<XmlNode>();
        if (concept.isNotMultiple()) {
            if (node.getOnValueConceptId() != null) {
                importedObservationNode.setAnswerConceptId(node.getOnValueConceptId());
            } else {
                importedObservationNode.removeAttribute("answerConceptId");
            }
            nodes.add(importedObservationNode);
            return nodes;
        } else {
            List<String> answers = concept.getAnswers();
            if (answers != null) {
                for (String answer : answers) {
                    XmlNode nodeWithAnswer = importedObservationNode.cloneNode();
                    nodeWithAnswer.setAttribute("answerConceptId", answer);
                    nodes.add(nodeWithAnswer);
                }
                return nodes;
            }

        }
        return null;
    }

}
