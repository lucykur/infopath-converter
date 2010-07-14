package org.openmrs.module.infopathconverter.rules;

import org.openmrs.module.infopathconverter.Action;
import org.openmrs.module.infopathconverter.Nodes;
import org.openmrs.module.infopathconverter.XmlNode;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class PatientRule extends Rule {

    public PatientRule(Document document) {
        super(document);
        addExpression("patient/patient.given_name", "patient.personName.givenName");
        addExpression("patient/patient.family_name", "patient.personName.familyName");
        addExpression("patient/patient.medical_record_number", "patient.patientIdentifier.identifier");

    }

    public void apply(Nodes nodes
    ) throws Exception {
        nodes.forEach(new Action<XmlNode>() {

            public void execute(XmlNode node) throws Exception {
                Element element = document.createElement("lookup");
                element.setAttribute("expression", getExpressionAsString(node.getBinding()));
                node.replace(element);
            }
        });
    }
}
