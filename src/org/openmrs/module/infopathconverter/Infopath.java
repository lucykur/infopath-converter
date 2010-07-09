package org.openmrs.module.infopathconverter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Infopath {
    private ZipInputStream stream;

    public Infopath(ZipInputStream stream) {
        this.stream = stream;
    }


    private InfopathForms extractForms() throws IOException {
        final InfopathForms forms = new InfopathForms();
        ZipEntry entry;
        while ((entry = stream.getNextEntry()) != null) {
            String name = entry.getName();
            if (name.endsWith(".xsl")) {
                forms.add(new InfopathForm(name, getContent()));
            }

            if(name.equals("template.xml")){
                forms.setTemplateXml(getContent());
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
        return extractForms().toHTML();
    }


}
