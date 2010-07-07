package org.openmrs.module.infopathconverter;

import org.openmrs.module.infopathconverter.xmlutils.XmlDocumentFactory;
import org.w3c.dom.Document;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

public class InfopathForms {
    private List<InfopathForm> forms;
    private String template;

    public InfopathForms() {
        forms = new ArrayList<InfopathForm>();
    }

    public void add(InfopathForm infopathForm) {
        forms.add(infopathForm);
    }

    public void setTemplateXml(String content) {
        this.template = content;
    }

    public String toString() {
        HtmlForm htmlForm = new HtmlForm();
        try {
            Document document = XmlDocumentFactory.createXmlDocumentFromStream(new ByteArrayInputStream(template.getBytes()));
            for (InfopathForm form : forms) {
                htmlForm.addPage(form.toPage(document));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

        return htmlForm.toString();

    }
}
