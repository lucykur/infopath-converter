package org.openmrs.module.infopathconverter.web.controller;

import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;

public class InfopathConverterModuleFormControllerTest {
    private final String infopathZip = "./test/org/openmrs/module/infopathconverter/include/infopath.zip";

    private String convert(String path) throws Exception {
        File input = new File(path);
        InputStream stream = new FileInputStream(input);
        MockMultipartHttpServletRequest multipartHttpServletRequest = new MockMultipartHttpServletRequest();
        multipartHttpServletRequest.addFile(new MockMultipartFile(input.getName(), stream));
        MultipartFile file = multipartHttpServletRequest.getFile(input.getName());
        InfopathConverterModuleFormController controller = new InfopathConverterModuleFormController();
        MockHttpServletRequest request = new MockHttpServletRequest();
        controller.convert(request.getSession(), file);
        return (String) request.getSession().getAttribute("htmlform");
    }

    @Test
    public void shouldReturnAValidXML() throws Exception {
        XMLUnit.buildControlDocument(convert(infopathZip));
    }

    @Test
    public void shouldContainPageElementWithTitle() throws Exception {
        String html = convert(infopathZip);
        assertXpathExists("/htmlform/page[@title='Page1.xsl']", html);
    }

    @Test
    public void shouldHavePatientTransformation() throws Exception {
        String htmlform = convert(infopathZip);
        assertXpathExists("//lookup[@expression='patient.personName.givenName']", htmlform);
        assertXpathExists("//lookup[@expression='patient.personName.familyName']", htmlform);
        assertXpathExists("//lookup[@expression='patient.patientIdentifier.identifier']", htmlform);

    }
    
    @Test
    public void shouldNotChangeQuotingInAttributes() throws Exception {
        String htmlForm = convert(infopathZip);
        assertXpathExists("//input[@value='&lt;- Select Provider']", htmlForm);
    }

    @Test
    @Ignore
    public void shouldDisplayErrorMessageForInvalidFiles() throws Exception {
        convert("./test/org/openmrs/module/infopathconverter/include/invalidInfopath.txt");
    }


}
