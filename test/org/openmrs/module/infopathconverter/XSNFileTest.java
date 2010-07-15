package org.openmrs.module.infopathconverter;

import org.junit.Assert;
import org.junit.Test;

public class XSNFileTest {
    @Test
    public void shouldReturnAListOfXSLFiles() throws Exception {
        XSNFile file = new XSNFile("./test/org/openmrs/module/infopathconverter/include/infopath.xsn");
        Assert.assertEquals(1, file.getForms().length());
    }

    @Test
    public void shouldReturnTheTemplateXml() throws Exception {
        XSNFile file = new XSNFile("./test/org/openmrs/module/infopathconverter/include/infopath.xsn");
        Assert.assertNotNull(file.getTemplateXml());    
    }
}
