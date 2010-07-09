package org.openmrs.module.infopathconverter;

import org.openmrs.module.infopathconverter.rules.Action;
import org.openmrs.module.infopathconverter.rules.observation.TemplateXml;
import org.openmrs.module.infopathconverter.xmlutils.XmlDocumentFactory;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Infopath {
    private ZipInputStream stream;
    private TemplateXml template;

    public Infopath(ZipInputStream stream) {
        this.stream = stream;
    }


    private InfopathForms extractForms() throws Exception, SAXException, ParserConfigurationException {
        final InfopathForms forms = new InfopathForms();
        ZipEntry entry;
        while ((entry = stream.getNextEntry()) != null) {
            String name = entry.getName();
            if (name.endsWith(".xsl")) {
                forms.add(new InfopathForm(name, getContent()));
            }

            if(name.equals("template.xml")){
                template = new TemplateXml(XmlDocumentFactory.createXmlDocumentFromStream(new ByteArrayInputStream(getContent().getBytes())));
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
        extractForms().forEach(new Action<InfopathForm>(){
            public void execute(InfopathForm form) throws Exception {
                  htmlForm.addPage(form.toPage(template));
            }
        });
        return htmlForm.toString();
    }


}
