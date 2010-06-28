package org.openmrs.module.infopathconverter.web.controller;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private MultipartFile file;

    public Infopath(MultipartFile file) {
        this.file = file;
    }

    public Rules parse() {
//        extractForms();
        return null;
    }

    private List<InfopathForm> extractForms() throws IOException {
        List<InfopathForm> infopathForms = new ArrayList<InfopathForm>();
        ZipInputStream inputStream = new ZipInputStream(file.getInputStream());
        ZipEntry zipEntry;
        while ((zipEntry = inputStream.getNextEntry()) != null) {
            if (zipEntry.getName().endsWith(".xsl")) {
                infopathForms.add(new InfopathForm(zipEntry.getName()));
            }
        }
        return infopathForms;
    }

    public List<InfopathForm> forms() throws IOException {
        return extractForms();
    }
}
