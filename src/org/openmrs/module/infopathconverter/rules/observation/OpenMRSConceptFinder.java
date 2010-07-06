package org.openmrs.module.infopathconverter.rules.observation;

import org.openmrs.module.infopathconverter.xmlutils.XmlDocumentFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;

public class OpenMRSConceptFinder {
    private Document xmlStream;

    public OpenMRSConceptFinder(String templateXml) throws Exception {
        xmlStream = XmlDocumentFactory.createXmlDocumentFromStream(new ByteArrayInputStream(templateXml.getBytes()));
    }

    public OpenMRSConcept findConcept(Node node) throws Exception {
        String bindingName = node.getAttributes().getNamedItem("xd:binding").getNodeValue();
        String[] bindingNameSegments = bindingName.split("/");
        return getOpenMRSConceptFromXML(bindingNameSegments);
    }

    private OpenMRSConcept getOpenMRSConceptFromXML(String[] bindingNameSegments) throws Exception {
        for (int i = 1; i < bindingNameSegments.length; i++) {
            NodeList openmrsConceptMatchList = xmlStream.getElementsByTagName(bindingNameSegments[i]);
            if (openmrsConceptMatchList.getLength() > 0) {
                Node node = openmrsConceptMatchList.item(0);
                if (node.getAttributes().getNamedItem("openmrs_concept") != null) {

                    Node isMultipleNode = node.getAttributes().getNamedItem("multiple");
                    String isMultiple = "0";
                    if (isMultipleNode != null) {
                        isMultiple = isMultipleNode.getNodeValue();
                    }

                    String openmrs_concept = node.getAttributes().getNamedItem("openmrs_concept").getNodeValue();

                    String openmrs_datatype = node.getAttributes().getNamedItem("openmrs_datatype").getNodeValue();

                    return new OpenMRSConcept(isMultiple, getConceptId(openmrs_concept), openmrs_datatype);
                }
            }

        }
        return null;
    }

    private String getConceptId(String openmrs_concept) {
        int index = openmrs_concept.indexOf('^');
        if (index != -1)
            return openmrs_concept.substring(0, index);
        return null;
    }
    
}
