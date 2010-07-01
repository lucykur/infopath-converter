package org.openmrs.module.infopathconverter;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import org.openmrs.module.infopathconverter.xmlutils.XmlDocumentFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HtmlForm {
    private List<Document> pages;
    private Map<String, String> namespaceMappings;

    public HtmlForm() {
        pages = new ArrayList<Document>();
        namespaceMappings = new HashMap<String, String>();
        namespaceMappings.put("xmlns:xsl", "http://www.w3.org/1999/XSL/Transform");
        namespaceMappings.put("xmlns:xd", "http://schemas.microsoft.com/office/infopath/2003");
    }

    public String toString() {
        Document htmlFormDocument;
        try {
            htmlFormDocument = XmlDocumentFactory.createEmptyXmlDocument();
            Node element = addHtmlFormElement(htmlFormDocument);
            for (Document page : pages) {
                element.appendChild(htmlFormDocument.importNode(page.getDocumentElement(), true));
            }
            return documentAsString(htmlFormDocument);

        } catch (Exception e) {
            return "";
        }
    }

    private Node addHtmlFormElement(Document document) {
        Node htmlFormNode = document.appendChild(document.createElement("htmlform"));

        for (Map.Entry<String, String> nsEntry : namespaceMappings.entrySet()) {
            Attr attr = document.createAttribute(nsEntry.getKey());
            attr.setNodeValue(nsEntry.getValue());
            htmlFormNode.getAttributes().setNamedItem(attr);
        }

        return htmlFormNode;
    }

    private String documentAsString(Document xmlDocument) throws IOException {
        OutputFormat format = new OutputFormat(xmlDocument);
        StringWriter writer = new StringWriter();
        XMLSerializer serializer = new XMLSerializer(writer, format);
        serializer.serialize(xmlDocument);
        return writer.toString();
    }


    public void addPage(Document page) {
        pages.add(page);
    }
}
