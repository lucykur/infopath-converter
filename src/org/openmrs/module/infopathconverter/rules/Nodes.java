package org.openmrs.module.infopathconverter.rules;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
        if(node != null){
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

}
    