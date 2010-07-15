package org.openmrs.module.infopathconverter;

import net.sf.jcablib.CabEntry;
import net.sf.jcablib.CabFile;
import net.sf.jcablib.CabFileInputStream;
import net.sf.jcablib.CabFolder;
import org.openmrs.module.infopathconverter.rules.observation.TemplateXml;

import java.io.*;
import java.util.Scanner;

public class XSNFile {
    private CabFile file;

    public XSNFile(String path) throws IOException {
        file = new CabFile(path);
    }


    public InfopathForms getForms() throws IOException {
        InfopathForms forms = new InfopathForms();
        for (CabEntry entry : file.getEntries()) {
            String name = entry.getName();
            if (name.endsWith(".xsl")) {
                forms.add(new InfopathForm(name, getContent(entry)));
            }
        }
        return forms;
    }

    private String getContent(CabEntry entry) throws IOException {
//        CabFileInputStream stream = new CabFileInputStream(entry.getCabFolder());
//        byte[] data = new byte[2048];
//
//        ByteArrayOutputStream writer = new ByteArrayOutputStream();
//        int count;
//        while ((count = stream.read(data, 0, 2048)) != -1) {
//            writer.write(data, 0, count);
//        }
//        return writer.toString();
        StringBuffer content = new StringBuffer();
//        FileReader reader = new FileReader(entry.getArchiveFile());
//        BufferedReader bufferedReader = new BufferedReader(reader);
//        String text;
//        while ((text = bufferedReader.readLine()) != null) {
//            content.append(text);
//        }

        CabFileInputStream stream = new CabFileInputStream(entry.getCabFolder());
        Scanner scanner = new Scanner(stream,"UTF-8");
        while(scanner.hasNextLine()){
            content.append(scanner.nextLine());
        }
        return content.toString();
    }

    public TemplateXml getTemplateXml() throws Exception {
         for (CabEntry entry : file.getEntries()) {
            String name = entry.getName();
            if (name.equals("template.xml")) {
                String content = getContent(entry);                
                return new TemplateXml(new XmlDocument(content));
            }
        }
        return null;
    }
}
