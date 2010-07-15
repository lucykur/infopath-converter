package org.openmrs.module.infopathconverter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class XSNFileTest {
    private FileInputStream stream;
    private XSNFile file;

    @Before
    public void setUp() throws Exception {
        stream = new FileInputStream(new File("./test/org/openmrs/module/infopathconverter/include/infopath.xsn"));
        file = new XSNFile(stream);
    }

    @Test
    public void shouldReturnAListOfXSLFiles() throws Exception {
        InfopathForms forms = file.getForms();
        Assert.assertEquals(1, forms.length());
    }

    @Test
    public void shouldReturnTheTemplateXml() throws Exception {
        Assert.assertNotNull(file.getTemplateXml());
    }

    @Test
    public void shouldReturnTheXsd() throws Exception {
        Assert.assertNotNull(file.getInfopathXsd());
    }

}
