package org.openmrs.module.infopathconverter.web.controller;

import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;

import java.io.File;
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
    private final String infopathZip = "./test/org/openmrs/module/infopathconverter/include/infopath.zip";

    private String convert(String path) throws IOException {
        File input = new File(path);
        InputStream stream = new FileInputStream(input);
        MockMultipartHttpServletRequest multipartHttpServletRequest = new MockMultipartHttpServletRequest();
        multipartHttpServletRequest.addFile(new MockMultipartFile(input.getName(), stream));
        MultipartFile file = multipartHttpServletRequest.getFile(input.getName());
        InfopathConverterModuleFormController controller = new InfopathConverterModuleFormController();
        ModelMap map = new ModelMap();
        controller.convert(map, null, file);
        return (String) map.get("htmlform");
    }

    @Test
    public void shouldReturnAValidXML() throws Exception {
        Document document = XMLUnit.buildControlDocument(convert(infopathZip));
    }

    @Test
    public void shouldContainPageElementWithTitle() throws Exception {
        assertXpathExists("/htmlform/page[@title='Page1.xsl']", convert(infopathZip));
    }

    @Test
    public void shouldHavePatientTransformation() throws Exception {
        String htmlform = convert(infopathZip);
        assertXpathExists("//lookup[@expression='patient.personName.givenName']", htmlform);
        assertXpathExists("//lookup[@expression='patient.personName.familyName']", htmlform);
        assertXpathExists("//lookup[@expression='patient.patientIdentifier.identifier']", htmlform);

    }

    @Test
    public void shouldDisplayErrorMessageForInvalidFiles() throws Exception {
         String htmlform = convert("./test/org/openmrs/module/infopathconverter/include/invalidInfopath.txt");
    }
}
