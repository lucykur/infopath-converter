package org.openmrs.module.infopathconverter.rules;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.Map;

public class EncounterRule implements Rule {
    private Map<String, String> encounterExpressionMap;

    public EncounterRule() {
        this.encounterExpressionMap = new HashMap<String, String>();
        encounterExpressionMap.put("encounter/encounter.encounter_datetime", "encounterDate");
//        encounterExpressionMap.put("encounter/encounter.location_id", "encounterLocation");
        encounterExpressionMap.put("encounter/encounter.provider_id", "encounterProvider");
    }

    public void apply(Document document, NodeList nodes, String observationCodedXml) {
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            String elementName = transformAttribute(node);
            if (elementName == null)
                node.getParentNode().removeChild(node);
            else {
                Element encounterElement = document.createElement(elementName);
                node.getParentNode().replaceChild(encounterElement, node);
            }
        }
    }


    private String transformAttribute(Node lookupNode) {
        String bindingName = lookupNode.getAttributes().getNamedItem("xd:binding").getNodeValue();
        for (String encounterName : encounterExpressionMap.keySet()) {
            if (bindingName.contains(encounterName)) {
                return encounterExpressionMap.get(encounterName);
            }
        }
        return null;
    }
}
