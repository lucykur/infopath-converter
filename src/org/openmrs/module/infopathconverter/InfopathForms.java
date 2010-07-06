package org.openmrs.module.infopathconverter;

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
        for (InfopathForm form : forms) {
            try {
                htmlForm.addPage(form.toPage(template));    
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        }

        return htmlForm.toString();

    }
}
