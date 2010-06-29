package org.openmrs.module.infopathconverter.web.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: lkurian
 * Date: Jun 28, 2010
 * Time: 2:44:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class Infopath {
    private ZipInputStream stream;

    public Infopath(ZipInputStream stream) {
        this.stream = stream;
    }


    private List<InfopathForm> extractForms() throws IOException {
        List<InfopathForm> forms = new ArrayList<InfopathForm>();
        ZipEntry entry;
        while ((entry = stream.getNextEntry()) != null) {
            if (entry.getName().endsWith(".xsl")) {
                byte[] data = new byte[2048];
                
                ByteArrayOutputStream writer = new ByteArrayOutputStream();
                int count;
                while ((count = stream.read(data, 0, 2048)) != -1) {
                    writer.write(data,0,count);
                }
                forms.add(new InfopathForm(entry.getName(),writer.toString()));
            }
        }
        return forms;
    }

    public String toHTMLForm() throws Exception {
        List<InfopathForm> forms = extractForms();
        HtmlForm htmlForm = new HtmlForm();
        for(InfopathForm form:forms){
            htmlForm.addPage(form.toPage());
        }
        return htmlForm.toString();
    }
}
