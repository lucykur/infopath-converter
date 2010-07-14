package org.openmrs.module.infopathconverter.rules;

import org.openmrs.module.infopathconverter.Action;
import org.openmrs.module.infopathconverter.Nodes;
import org.openmrs.module.infopathconverter.XmlNode;
import org.w3c.dom.Document;

public class SubmitButtonRule extends Rule {
    public SubmitButtonRule(Document document) {
        super(document);
    }

    public void apply(Nodes nodes) throws Exception {
        nodes.forEach(new Action<XmlNode>() {
            public void execute(XmlNode node) throws Exception {
                node.replace(document.createElement("submit"));
            }
        }
        );
    }
}
