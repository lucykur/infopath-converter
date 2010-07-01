package org.openmrs.module.infopathconverter.rules;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.Map;


public class PatientRules {
    private Map<String, String> patientExpressionMap;

    public PatientRules() {
        this.patientExpressionMap = new HashMap<String, String>();
        patientExpressionMap.put("patient/patient.given_name", "patient.personName.givenName");
        patientExpressionMap.put("patient/patient.family_name", "patient.personName.familyName");
        patientExpressionMap.put("patient/patient.medical_record_number", "patient.patientIdentifier.identifier");
    }

    public void apply(Document page, NodeList nodes) {
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            Element lookupElement = page.createElement("lookup");
            lookupElement.setAttribute("expression", transformAttribute(node));
            node.getParentNode().replaceChild(lookupElement, node);
        }
    }

    private String transformAttribute(Node lookupNode) {
        String bindingName = lookupNode.getAttributes().getNamedItem("xd:binding").getNodeValue();
        return patientExpressionMap.get(bindingName);
    }
}
