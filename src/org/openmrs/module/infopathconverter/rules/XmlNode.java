package org.openmrs.module.infopathconverter.rules;

import org.openmrs.module.infopathconverter.xmlutils.XPathUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathExpressionException;
import java.util.ArrayList;
import java.util.List;

public class XmlNode {
    private Node node;

    public XmlNode(Node node) {
        this.node = node;
    }

    public String getAttribute(String name) {
        String value = "";
        try {
            value = node.getAttributes().getNamedItem(name).getNodeValue();
        } catch (Exception e) {
        }
        return value;

    }

    public Nodes getChildNodes() {
        return new Nodes(node.getChildNodes());
    }

    public String getConcept() {
        return getAttribute("openmrs_concept");
    }

    public String getConceptId() {
        return getId(getConcept());
    }

    private String getId(String concept) {
        int index = concept.indexOf('^');
        if (index != -1)
            return concept.substring(0, index);
        return null;
    }

    private String getMultiple() {
        String multiple = getAttribute("multiple");
        return multiple == "" ? "0" : multiple;
    }

    public String getDataType() {
        return getAttribute("openmrs_datatype");
    }

    public String getBinding() {
        return getAttribute("xd:binding");
    }

    public void forEachBindingSegment(Document document, final NodeAction action) throws XPathExpressionException {
        String[] segments = getBinding().split("/");
        for (int i = 1; i < segments.length; i++) {
            NodeList list = XPathUtils.matchNodes(document, String.format("//%s[@openmrs_concept and @openmrs_datatype != 'ZZ']", segments[i]));
            new Nodes(list).forEach(new NodeAction() {
                @Override
                public void execute(XmlNode node) throws Exception {
                    action.execute(node);
                }
            });
        }

    }

    public List<String> getAnswers() {
        final List<String> answers = new ArrayList<String>();

        getChildNodes().forEach(new NodeAction() {
            public void execute(XmlNode node) {
                if (node.getConcept() != null)
                    answers.add(node.getConceptId());
            }
        });

        return answers;
    }

    public void  appendChild (XmlNode node) {
        this.node.getParentNode().appendChild(node.getNode());
    }

    private Node getNode() {
        return node;
    }

    public void remove() {
        node.getParentNode().removeChild(node);
    }

    public String getOnValueConceptId() {
        return getId(getAttribute("xd:onValue"));
    }

    public String getOnValue() {
        return getAttribute("xd:onValue");
    }

    public boolean isMultiple() {
        return "1".equals(getMultiple());
    }

    public boolean hasMultiple() {
        return (getMultiple() != null && !"".equals(getMultiple()));
    }

    public void replace(Element element) {
        node.getParentNode().replaceChild(element, node);
    }

    public void setAttribute(String key, String value) {
        node.getAttributes().getNamedItem(key).setNodeValue(value);
    }

    public boolean isNotMultiple() {
        return !isMultiple();
    }

    public void removeAttribute(String name) {
        node.getAttributes().removeNamedItem(name);
    }

    public XmlNode cloneNode() {
        return new XmlNode(node.cloneNode(true));
    }

    public void setAnswerConceptId(String conceptId) {
        setAttribute("answerConceptId", conceptId);
    }

    public XmlNode getParentNode() {
        return new XmlNode(node.getParentNode());
    }

    public void setConceptId(String id) {
        setAttribute("conceptId",id);
    }
}
