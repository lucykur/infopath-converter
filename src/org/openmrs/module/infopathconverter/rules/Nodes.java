package org.openmrs.module.infopathconverter.rules;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class Nodes {
    private NodeList nodes;

    public Nodes(NodeList nodes) {

        this.nodes = nodes;
    }

    public void forEach(Action<XmlNode> action) throws Exception {
        for (int i = 0; i < nodes.getLength(); i++) {
            action.execute(new XmlNode(nodes.item(i)));
        }

    }

    public XmlNode getNode(int i) {
        return new XmlNode(nodes.item(i));
    }

    public NodeList getNodes() {
        return nodes;
    }

    public void forFirstNode(Action<XmlNode> action) throws Exception {
        Node node = nodes.item(0);
        if (node != null) {
            action.execute(new XmlNode(node));
        }
    }

    public void detachFromParent() throws Exception {
        forEach(new Action<XmlNode>() {
            public void execute(XmlNode node) throws Exception {
                node.remove();
            }
        });
    }

    public String joinAttributeValue(final ReturnAction<String> action) throws Exception {
        final List<String> values = new ArrayList<String>();

        forEach(new Action<XmlNode>() {

            public void execute(XmlNode node) throws Exception {
                values.add(action.execute(node));

            }
        });
        return join(values, ",");

    }

    private String join(Iterable<?> elements, String delimiter) {
        StringBuilder sb = new StringBuilder();
        for (Object e : elements) {
            if (sb.length() > 0)
                sb.append(delimiter);
            sb.append(e);
        }
        return sb.toString();
    }

}
    