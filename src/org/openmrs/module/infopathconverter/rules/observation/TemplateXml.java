package org.openmrs.module.infopathconverter.rules.observation;

import org.openmrs.module.infopathconverter.xmlutils.XPathUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;
import java.util.List;

public class TemplateXml {
    private Document document;

    public TemplateXml(Document document) throws Exception {
        this.document = document;
    }

    public OpenMRSConcept findConcept(Node node) throws Exception {
        return new BindingNode(node).
                forEachSegment(
                        new NodeAction() {
                            OpenMRSConcept execute(String segment) {
                                try {
                                    //TODO: Improve performance
                                    NodeList list = XPathUtils.matchNodes(document, String.format("//%s[@openmrs_concept and @openmrs_datatype != 'ZZ']", segment));
                                    return new SegmentNode(list).toOpenMRSConcept();
                                } catch (XPathExpressionException e) {
                                    return null;
                                }
                            }
                        });


    }


    private class BindingNode {
        private String binding;

        public BindingNode(Node node) {
            binding = node.getAttributes().getNamedItem("xd:binding").getNodeValue();
        }

        public OpenMRSConcept forEachSegment(NodeAction action) {
            String[] segments = binding.split("/");
            OpenMRSConcept concept = null;
            for (int i = 1; i < segments.length; i++) {
                concept = action.execute(segments[i]);
                if (concept != null) break;
            }
            return concept;
        }
    }

    private abstract class NodeAction {
        abstract OpenMRSConcept execute(String segment);
    }

    private class SegmentNode {
        private Node node;

        public SegmentNode(NodeList list) {
            if (list.getLength() > 0) {
                node = list.item(0);
            }
        }

        public OpenMRSConcept toOpenMRSConcept() {
            if (node == null) return null;
            return new OpenMRSConcept(isMultiple(), getConceptId(), getDataType(), getAnswers());

        }

        private String isMultiple() {
            String multiple = getAttribute("multiple");
            return multiple == "" ? "0" : multiple;
        }

        private String getConcept() {
            return getAttribute("openmrs_concept");
        }

        private String getDataType() {
            return getAttribute("openmrs_datatype");
        }

        private String getAttribute(String attribute) {
            String value = "";
            try {
                value = node.getAttributes().getNamedItem(attribute).getNodeValue();
            } catch (Exception e) {
            }
            return value;
        }

        private List<String> getAnswers() {
            NodeList childNodes = node.getChildNodes();
            List<String> answers = new ArrayList<String>();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node node = childNodes.item(i);
                if (node.getAttributes() != null) {
                    Node openmrs_concept = node.getAttributes().getNamedItem("openmrs_concept");
                    if (openmrs_concept != null)
                        answers.add(getId(openmrs_concept.getNodeValue()));
                }
            }
            return answers;
        }

        private String getConceptId() {
            String concept = getConcept();
            return getId(concept);
        }

        private String getId(String concept) {
            int index = concept.indexOf('^');
            if (index != -1)
                return concept.substring(0, index);
            return null;
        }

    }
}
