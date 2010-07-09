package org.openmrs.module.infopathconverter.rules.observation;

import org.openmrs.module.infopathconverter.rules.Action;
import org.openmrs.module.infopathconverter.rules.XmlNode;
import org.openmrs.module.infopathconverter.xmlutils.XPathUtils;
import org.w3c.dom.Document;

public class TemplateXml {
    private Document document;

    public TemplateXml(Document document) throws Exception {
        this.document = document;
    }

    public void findConcept(XmlNode node, final Action<XmlNode> action) throws Exception {

        node.forEachBindingSegment(new Action<String>() {
            public void execute(String segment) throws Exception {
                XPathUtils.matchNodes(document, String.format("//%s[@openmrs_concept and @openmrs_datatype != 'ZZ']", segment)).forEach(new Action<XmlNode>() {

                    public void execute(XmlNode node) throws Exception {
                        action.execute(node);
                    }
                });

            }
        });
    }

}