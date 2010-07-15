package org.openmrs.module.infopathconverter;

import org.openmrs.module.infopathconverter.rules.observation.InfopathXsd;
import org.openmrs.module.infopathconverter.rules.observation.TemplateXml;

import java.io.FileInputStream;
import java.io.IOException;

public class XSNFile {
    private CabFile file;

    public XSNFile(FileInputStream stream) throws IOException {
        file = new CabFile(stream);
    }


    public InfopathForms getForms() throws Exception {
        final InfopathForms forms = new InfopathForms();
        file.forEachEntry(new Action<CabEntry>() {
            public void execute(CabEntry entry) throws Exception {
                if (entry.isXSL()) {
                    String content = entry.getContents();
                    forms.add(new InfopathForm(entry.getName(), content));
                }
            }
        });
        return forms;
    }


    public TemplateXml getTemplateXml() throws Exception {
        final TemplateXml[] templateXml = {null};
        file.forEachEntry(new Action<CabEntry>() {
            public void execute(CabEntry entry) throws Exception {
                if (entry.isTemplateXml()) {
                    templateXml[0] = new TemplateXml(new XmlDocument(entry.getContents()));
                }
            }
        });
        return templateXml[0];
    }


    public InfopathXsd getInfopathXsd() throws Exception {
        final InfopathXsd[] xsd = {null};
        file.forEachEntry(new Action<CabEntry>(){
            public void execute(CabEntry entry) throws Exception {
                if(entry.isXsd()){
                    xsd[0] = new InfopathXsd(new XmlDocument(entry.getContents()));
                }
            }
        });
        return xsd[0];
    }
}
