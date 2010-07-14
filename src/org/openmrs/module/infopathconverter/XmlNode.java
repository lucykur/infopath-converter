package org.openmrs.module.infopathconverter;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

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
        return "";
    }

    private String getMultiple() {
        return getAttribute("multiple");
    }

    public String getDataType() {
        return getAttribute("openmrs_datatype");
    }

    public String getBinding() {
        return getAttribute("xd:binding");
    }

    public void forLastBindingSegment(final Action action) throws Exception {
        String[] segments = getBinding().split("/");
        if (segments.length > 2)
            action.execute(segments[segments.length - 2]);

    }

    public List<String> getAnswers() throws Exception {
        final List<String> answers = new ArrayList<String>();

        getChildNodes().forEach(new Action<XmlNode>() {
            public void execute(XmlNode node) {
                if (node.getConcept() != null)
                    answers.add(node.getConceptId());
            }
        });

        return answers;
    }

    public void appendChild(XmlNode child) {
        Node parent = node.getParentNode();
        if (parent != null)
            parent.appendChild(child.getNode());
    }

    private Node getNode() {
        return node;
    }

    public void remove() {
        Node parent = node.getParentNode();
        if (parent != null)
            parent.removeChild(node);
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
        Node parent = node.getParentNode();
        if (parent != null)
            parent.replaceChild(element, node);
    }

    private void setAttribute(String key, String value) {
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
        if (!"".equals(conceptId))
            setAttribute("answerConceptId", conceptId);
    }

    public void setConceptId(String id) {
        setAttribute("conceptId", id);
    }

    public void forEachAnswer(Action<String> action) throws Exception {
        for (String answer : getAnswers()) {
            action.execute(answer);
        }

    }

    public boolean hasOnValueConceptId() {
        return !"".equals(getOnValueConceptId());
    }

    public boolean isRadio() {
        return "radio".equals(getAttribute("type"));
    }

    public String getBindingType() {
        String[] bindings = getBinding().split("/");
        return String.format("%s_type", bindings[bindings.length - 2]);
    }

    public String getValueId() {
        return getId(getAttribute("value"));
    }
}
