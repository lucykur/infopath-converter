package org.openmrs.module.infopathconverter.rules.observation;

import org.openmrs.module.infopathconverter.rules.Action;
import org.openmrs.module.infopathconverter.rules.Nodes;
import org.openmrs.module.infopathconverter.rules.Rule;
import org.openmrs.module.infopathconverter.rules.XmlNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;

public class ObservationRule extends Rule {
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
        nodes.forEach(new Action<XmlNode>() {
            public void execute(final XmlNode node) throws Exception {
                templateXml.findConcept(node, new Action<XmlNode>() {
                    public void execute(XmlNode concept) throws Exception {
                        XmlNode observation = getExpressionAsNode(concept.getDataType());
                        if (observation != null) {
                            if (concept.hasMultiple()) {
                                observation.setConceptId(concept.getConceptId());
                                createTransformedNodes(node, concept, observation);
                            } else {
                                node.remove();
                            }

                        }
                    }
                });

            }
        });

    }

    private void createTransformedNodes(XmlNode node, XmlNode concept, XmlNode observation) throws Exception {
        ArrayList<XmlNode> answers = getNodesWithAnswerConcepts(node, concept, observation);
        for (XmlNode answer : answers) {
            node.appendChild(answer);
        }
        node.remove();
    }

    private ArrayList<XmlNode> getNodesWithAnswerConcepts(XmlNode node, XmlNode concept, final XmlNode observation) throws Exception {
        final ArrayList<XmlNode> nodes = new ArrayList<XmlNode>();
        if (concept.isNotMultiple()) {
            XmlNode clone = observation.cloneNode();
            if (node.hasOnValueConceptId()) {
                clone.setAnswerConceptId(node.getOnValueConceptId());
            } else {
                clone.removeAttribute("answerConceptId");
            }
            nodes.add(clone);
        } else {
            concept.forEachAnswer(new Action<String>() {
                public void execute(String answer) throws Exception {
                    XmlNode nodeWithAnswer = observation.cloneNode();
                    nodeWithAnswer.setAnswerConceptId(answer);
                    nodes.add(nodeWithAnswer);
                }
            });
        }
        return nodes;
    }

}
