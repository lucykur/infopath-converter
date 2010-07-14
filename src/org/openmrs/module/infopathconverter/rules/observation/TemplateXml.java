package org.openmrs.module.infopathconverter.rules.observation;

import org.openmrs.module.infopathconverter.Action;
import org.openmrs.module.infopathconverter.XmlDocument;
import org.openmrs.module.infopathconverter.XmlNode;

public class TemplateXml {
    private XmlDocument document;

    public TemplateXml(XmlDocument document) throws Exception {
        this.document = document;
    }

    public void findConcept(XmlNode node, final Action<XmlNode> action) throws Exception {

        node.forLastBindingSegment(new Action<String>() {
            public void execute(String segment) throws Exception {
                document.match(String.format("//%s[@openmrs_concept and @openmrs_datatype != 'ZZ']", segment)).forEach(new Action<XmlNode>() {

                    public void execute(XmlNode node) throws Exception {
                        action.execute(node);
                    }
                });

            }
        });
    }

}

