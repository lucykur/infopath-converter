package org.openmrs.module.infopathconverter.rules.observation;

import org.openmrs.module.infopathconverter.XmlDocument;
import org.openmrs.module.infopathconverter.ReturnAction;
import org.openmrs.module.infopathconverter.XmlNode;

import java.util.HashMap;

public class InfopathXsd {
    private XmlDocument document;

    public InfopathXsd(XmlDocument document) {
        this.document = document;
    }

    public String getAnswerIds(String type) throws Exception {
        HashMap<String, String> types = new HashMap<String, String>();
        if (!types.containsKey(type)) {
            String ids = document.match(String.format("//xs:complexType[@name='%s']//xs:enumeration", type)).joinAttributeValue(new ReturnAction<String>() {

                public String execute(XmlNode node) {
                    return node.getValueId();
                }
            });
            types.put(type, ids);
        }
        return types.get(type);
    }
}
