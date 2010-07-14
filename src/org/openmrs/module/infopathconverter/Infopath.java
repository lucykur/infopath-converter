package org.openmrs.module.infopathconverter;

import org.openmrs.module.infopathconverter.rules.observation.InfopathXsd;
import org.openmrs.module.infopathconverter.rules.observation.TemplateXml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Infopath {
    private ZipInputStream stream;
    private TemplateXml template;
    private InfopathXsd xsd;

    public Infopath(ZipInputStream stream) {
        this.stream = stream;
    }


    private InfopathForms extractForms() throws Exception {
        final InfopathForms forms = new InfopathForms();
        ZipEntry entry;
        while ((entry = stream.getNextEntry()) != null) {
            String name = entry.getName();
            if (name.endsWith(".xsl")) {
                forms.add(new InfopathForm(name, getContent()));
            }

            if (name.equals("template.xml")) {
                template = new TemplateXml(new XmlDocument(getContent()));
            }

            if (name.endsWith(".xsd")) {
                xsd = new InfopathXsd(new XmlDocument(getContent()));
            }


        }

        return forms;
    }

    private String getContent() throws IOException {
        byte[] data = new byte[2048];

        ByteArrayOutputStream writer = new ByteArrayOutputStream();
        int count;
        while ((count = stream.read(data, 0, 2048)) != -1) {
            writer.write(data, 0, count);
        }
        return writer.toString();
    }

    public String toHTMLForm() throws Exception {
        final HtmlForm htmlForm = new HtmlForm();
        extractForms().forEach(new Action<InfopathForm>() {
            public void execute(InfopathForm form) throws Exception {
                htmlForm.addPage(form.toPage(template, xsd));
            }
        });
        return htmlForm.toString();
    }


}
