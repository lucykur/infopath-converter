package org.openmrs.module.infopathconverter;

import org.openmrs.module.infopathconverter.rules.observation.InfopathXsd;
import org.openmrs.module.infopathconverter.rules.observation.TemplateXml;

import java.io.IOException;
import java.io.InputStream;

public class Infopath {
    private XSNFile file;

    public Infopath(InputStream stream) throws IOException {
        file = new XSNFile(stream);
    }


    public String toHTMLForm() throws Exception {
        final InfopathXsd xsd = file.getInfopathXsd();
        final TemplateXml template = file.getTemplateXml();
        final HtmlForm htmlForm = new HtmlForm();
        file.getForms().forEach(new Action<InfopathForm>() {
            public void execute(InfopathForm form) throws Exception {
                htmlForm.addPage(form.toPage(template, xsd));
            }
        });
        return htmlForm.toString();
    }


}
