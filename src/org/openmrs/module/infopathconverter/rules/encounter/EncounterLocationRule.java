package org.openmrs.module.infopathconverter.rules.encounter;

import org.openmrs.module.infopathconverter.rules.Action;
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


    public void apply(final Nodes nodes) throws Exception {
        nodes.forFirstNode(new Action<XmlNode>() {
            public void execute(XmlNode node) throws Exception {
                Element element = document.createElement("encounterLocation");
                element.setAttribute("order", getOrders(nodes));
                node.appendChild(new XmlNode(element));
            }
        });

        nodes.detachFromParent();
    }

    public String getOrders(Nodes nodes) throws Exception {
        final List<String> orders = new ArrayList<String>();

        nodes.forEach(new Action<XmlNode>() {

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

    private class EncounterNodes {
        private Nodes nodes;

        public EncounterNodes(Nodes nodes) {
            this.nodes = nodes;
        }


        public XmlNode getFirstNode() {
            return nodes.getNode(0);
        }

    }
}
