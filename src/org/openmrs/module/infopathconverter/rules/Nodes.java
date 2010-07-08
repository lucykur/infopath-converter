package org.openmrs.module.infopathconverter.rules;

import org.w3c.dom.NodeList;

public class Nodes {
    private NodeList nodes;

    public Nodes(NodeList nodes) {

        this.nodes = nodes;
    }

    public void forEach(NodeAction action) {
        for (int i = 0; i < nodes.getLength(); i++) {
            try {
                action.execute(new XmlNode(nodes.item(i)));
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

    }

    public XmlNode getNode(int i) {
        return new XmlNode(nodes.item(i));
    }
}
    