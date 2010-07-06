package org.openmrs.module.infopathconverter.rules;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class EncounterLocationRule implements Rule {
    public void apply(Document page, NodeList nodes) throws Exception {
        EncounterNodes encounterNodes = new EncounterNodes(nodes);
        Node parentNode = encounterNodes.getParentNode();
        Element element = page.createElement("encounterLocation");
        element.setAttribute("order", encounterNodes.getOrders());
        parentNode.appendChild(element);
        encounterNodes.detachFromParent();

    }


    private class EncounterNodes {
        private NodeList nodes;

        public EncounterNodes(NodeList nodes) {
            this.nodes = nodes;
        }

        public String getOrders() {
            List<String> orders = new ArrayList<String>();
            for (int i = 0; i < nodes.getLength(); i++) {
                Node location = nodes.item(i);
                orders.add(location.getAttributes().getNamedItem("xd:onValue").getNodeValue());
            }
            return join(orders, ",");
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

        public Node getParentNode() {
            return nodes.item(0).getParentNode();
        }

        public void detachFromParent() {
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                node.getParentNode().removeChild(node);
            }
        }
    }
}
