package org.openmrs.module.infopathconverter.rules.encounter;

import org.openmrs.module.infopathconverter.rules.Action;
import org.openmrs.module.infopathconverter.rules.Nodes;
import org.openmrs.module.infopathconverter.rules.Rule;
import org.openmrs.module.infopathconverter.rules.XmlNode;
import org.w3c.dom.Document;

public class EncounterRule extends Rule {

    public EncounterRule(Document document) {
        super(document);
        addExpression("encounter/encounter.encounter_datetime", "encounterDate");
        addExpression("encounter/encounter.provider_id", "encounterProvider");

    }

    public void apply(Nodes nodes) throws Exception {
        nodes.forEach(new Action<XmlNode>() {

            public void execute(XmlNode node) throws Exception {

                node.replace(document.createElement(searchExpression(node.getBinding())));

            }
        });
    }


}
