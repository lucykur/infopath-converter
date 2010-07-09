package org.openmrs.module.infopathconverter.rules.encounter;

import org.openmrs.module.infopathconverter.rules.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class EncounterLocationRule extends Rule {
    public EncounterLocationRule(Document document) {
        super(document);
    }


    public void apply(final Nodes nodes) throws Exception {
        nodes.forFirstNode(new Action<XmlNode>() {
            public void execute(XmlNode node) throws Exception {
                Element element = document.createElement("encounterLocation");
                element.setAttribute("order", nodes.joinAttributeValue(new ReturnAction<String>() {
                    public String execute(XmlNode node) {
                        return node.getOnValue();
                    }
                }));
                node.appendChild(new XmlNode(element));
            }
        });

        nodes.detachFromParent();
    }
}
