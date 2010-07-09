package org.openmrs.module.infopathconverter.rules.observation;

import org.openmrs.module.infopathconverter.rules.ReturnAction;
import org.openmrs.module.infopathconverter.rules.XmlNode;
import org.openmrs.module.infopathconverter.xmlutils.XPathUtils;
import org.w3c.dom.Document;

import java.util.HashMap;

public class InfopathXsd {
    private Document document;
    private HashMap<String, String> types;

    public InfopathXsd(Document document) {
        this.document = document;
    }

    public String getAnswerIds(String type) throws Exception {
        types = new HashMap<String, String>();
        if (!types.containsKey(type)) {
            String ids = XPathUtils.matchNodes(document, String.format("//xs:complexType[@name='%s']//xs:enumeration", type)).joinAttributeValue(new ReturnAction<String>() {

                public String execute(XmlNode node) {
                    return node.getValueId();
                }
            });
            types.put(type, ids);
        }
        return types.get(type);
    }
}
