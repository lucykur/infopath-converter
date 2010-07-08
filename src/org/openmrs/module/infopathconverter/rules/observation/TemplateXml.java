package org.openmrs.module.infopathconverter.rules.observation;

import org.openmrs.module.infopathconverter.rules.NodeAction;
import org.openmrs.module.infopathconverter.rules.XmlNode;
import org.w3c.dom.Document;

public class TemplateXml {
    private Document document;

    public TemplateXml(Document document) throws Exception {
        this.document = document;
    }

    public XmlNode findConcept(XmlNode node) throws Exception {

        final XmlNode[] concept = new XmlNode[1];
        node.forEachBindingSegment(document, new NodeAction() {
            @Override
            public void execute(XmlNode node) throws Exception {
                concept[0] = node;
            }
        });
        return concept[0];
    }


}
