package org.openmrs.module.infopathconverter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class CabEntry {
    private File file;

    public CabEntry(File file) {
        this.file = file;
    }

    public boolean isXSL() {
        return getName().endsWith(".xsl");
    }

    public String getName() {
        return file.getName();
    }

    public String getContents() throws IOException {
        FileInputStream stream = new FileInputStream(file);
        byte[] data = new byte[2048];

        ByteArrayOutputStream writer = new ByteArrayOutputStream();
        int count;
        while ((count = stream.read(data, 0, 2048)) != -1) {
            writer.write(data, 0, count);
        }
        return writer.toString();
    }

    public boolean isTemplateXml() {
        return getName().equals("template.xml");
    }

    public boolean isXsd() {
        return getName().endsWith(".xsd");
    }
}
