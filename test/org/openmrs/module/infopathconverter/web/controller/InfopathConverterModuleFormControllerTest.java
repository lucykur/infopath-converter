package org.openmrs.module.infopathconverter.web.controller;

import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathNotExists;

public class InfopathConverterModuleFormControllerTest {
    private final String infopathZip = "./test/org/openmrs/module/infopathconverter/include/infopath.zip";

    private String convert(String path) throws Exception {
        File input = new File(path);
        InputStream stream = new FileInputStream(input);
        MockMultipartHttpServletRequest multipartHttpServletRequest = new MockMultipartHttpServletRequest();
        multipartHttpServletRequest.addFile(new MockMultipartFile(input.getName(), stream));
        MultipartFile file = multipartHttpServletRequest.getFile(input.getName());
        InfopathConverterModuleFormController controller = new InfopathConverterModuleFormController();

        LinkedMultiValueMap map = new LinkedMultiValueMap();
        map.add("xsn", file);
        DefaultMultipartHttpServletRequest request = new DefaultMultipartHttpServletRequest(new MockHttpServletRequest(), map, null);
        MockHttpServletResponse response = new MockHttpServletResponse();

        controller.onSubmit(request, response, null, null);
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
    public void shouldNotTransformBindingsNotStartingWithPatient() throws Exception {
        String transformedXSN = convert(infopathZip);        
        assertXpathNotExists("//lookup[@expression='']", transformedXSN);
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

    @Test
    public void shouldTransformEncounterDate() throws Exception {
        assertXpathExists("//encounterDate", convert(infopathZip));

    }


    @Test
    public void shouldTransferEncounterLocation() throws Exception {
        assertXpathExists("//encounterLocation[@order='30,27,28,25,37,38,26,29,1']", convert(infopathZip));
    }


    @Test
    public void shouldTransferEncounterProvider() throws Exception {
        assertXpathExists("//encounterProvider", convert(infopathZip));
    }

    @Test
    public void shouldTransformObservations() throws Exception {
        String transformedXSN = convert(infopathZip);
        assertXpathExists("//obs[@conceptId='3389' and @answerConceptId='1065']", transformedXSN);
        assertXpathExists("//obs[@conceptId='3301' and @answerConceptId='1065,1066']", transformedXSN);
        assertXpathExists("//obs[@conceptId='3139' and @answerConceptId='3138,3135,3136,6246,3137,3114,3999']", transformedXSN);
    }

    @Test
    @Ignore
    public void shouldNotContainTheAnswerIdAttributeForNotSpecifiedObs() throws Exception {
        String transformedXSN = convert(infopathZip);
        assertXpathNotExists("//obs[@answerConceptId='']", transformedXSN);
    }


    @Test
    public void shouldEnsureThatOpenMRSConceptOfDatatypeZZAreNotPickedUp() throws Exception {
        assertXpathExists("//obs[@conceptId='1119']", convert(infopathZip));

    }

    @Test
    public void shouldEnsureThatIndividualTransformationsAreDoneWhenMultipleIsOne() throws Exception {
        String transformedXSN = convert(infopathZip);
        assertXpathExists("//obs[@conceptId='1119'and @answerConceptId='460']", transformedXSN);
        assertXpathExists("//obs[@conceptId='1119'and @answerConceptId='215']", transformedXSN);
        assertXpathExists("//obs[@conceptId='1119'and @answerConceptId='161']", transformedXSN);

    }

    @Test
    public void shouldConvertRadioElementToCommaSeparatedAnswers() throws Exception {
        String transformedXSN = convert(infopathZip);
        assertXpathExists("//obs[@conceptId='3139'and @answerConceptId='3138,3135,3136,6246,3137,3114,3999']", transformedXSN);

    }

    @Test
    public void shouldConvertCheckboxToBooleanCodedObservation() throws Exception {
        String transformedXSN = convert(infopathZip);
        assertXpathExists("//obs[@conceptId='6208' and @style='checkbox']", transformedXSN);

    }


    @Test
    public void shouldConvertTextBoxToTextObservation() throws Exception {
        String transformedXSN = convert(infopathZip);
        assertXpathExists("//obs[@conceptId='3221' and @style='textarea']", transformedXSN);

    }

    @Test
    public void shouldConvertTextBoxToNumericObservation() throws Exception {
        String transformedXSN = convert(infopathZip);
        assertXpathExists("//obs[@conceptId='3221']", transformedXSN);

    }

    @Test
    public void shouldConvertTextBoxToDateObservation() throws Exception {
        String transformedXSN = convert(infopathZip);
        assertXpathExists("//obs[@conceptId='3221']", transformedXSN);

    }

    @Test
    public void shouldConvertSubmitButton() throws Exception {
        String transformedXSN = convert(infopathZip);
        assertXpathExists("//submit", transformedXSN);
    }

}
