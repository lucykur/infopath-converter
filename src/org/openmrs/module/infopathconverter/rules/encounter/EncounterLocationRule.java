package org.openmrs.module.infopathconverter.rules.encounter;

import org.openmrs.module.infopathconverter.rules.NodeAction;
import org.openmrs.module.infopathconverter.rules.Nodes;
import org.openmrs.module.infopathconverter.rules.Rule;
import org.openmrs.module.infopathconverter.rules.XmlNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

public class EncounterLocationRule extends Rule {
    public EncounterLocationRule(Document document) {
        super(document);
    }


    public void apply(Nodes nodes) throws Exception {
        EncounterNodes encounterNodes = new EncounterNodes(nodes);
        XmlNode parentNode = encounterNodes.getFirstNode();
        Element element = document.createElement("encounterLocation");
        element.setAttribute("order", encounterNodes.getOrders());
        parentNode.appendChild(new XmlNode(element));
        encounterNodes.detachFromParent();
    }


    private class EncounterNodes {
        private Nodes nodes;

        public EncounterNodes(Nodes nodes) {
            this.nodes = nodes;
        }

        public String getOrders() {
            final List<String> orders = new ArrayList<String>();

            nodes.forEach(new NodeAction() {

                public void execute(XmlNode node) throws Exception {
                    orders.add(node.getOnValue());

                }
            });
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

        public XmlNode getFirstNode() {
            return nodes.getNode(0);
        }

        public void detachFromParent() {
            nodes.forEach(new NodeAction() {
                public void execute(XmlNode node) throws Exception {
                    node.remove();
                }
            });
        }
    }
}
