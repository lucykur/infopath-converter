package org.openmrs.module.infopathconverter.web.controller;

import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;

/**
 * Created by IntelliJ IDEA.
 * User: lkurian
 * Date: Jun 28, 2010
 * Time: 11:25:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class InfopathConverterModuleFormControllerTest {
    private String htmlform;

    @Before
    public void createInputStream() throws IOException {
        InputStream stream = new FileInputStream("./test/org/openmrs/module/infopathconverter/include/infopath.zip");
        MockMultipartHttpServletRequest multipartHttpServletRequest = new MockMultipartHttpServletRequest();
        multipartHttpServletRequest.addFile(new MockMultipartFile("infopath.zip", stream));
        MultipartFile file = multipartHttpServletRequest.getFile("infopath.zip");
        InfopathConverterModuleFormController controller = new InfopathConverterModuleFormController();
        ModelMap map = new ModelMap();
        controller.convert(map, file);
        htmlform = (String) map.get("htmlform");
    }

    @Test
    public void shouldReturnAValidXML() throws Exception {
        Document document = XMLUnit.buildControlDocument(htmlform);
    }

    @Test
    public void shouldContainPageElementWithTitle() throws Exception {
        assertXpathExists("/htmlform/page[@title='Page1.xsl']", htmlform);
    }

    @Test
    public void shouldHavePatientTransformation() throws Exception {
        assertXpathExists("//lookup[@expression='patient.personName.givenName']", htmlform);
    }
}
