package org.openmrs.module.infopathconverter.web.controller;

import org.custommonkey.xmlunit.XMLUnit;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openmrs.test.Verifies;
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

import static org.custommonkey.xmlunit.XMLAssert.*;

public class InfopathConverterModuleFormControllerTest {
    private static final String infopathZip = "./test/org/openmrs/module/infopathconverter/include/infopath.xsn";
    private static final String EMPTY_HTML_FORM = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<htmlform xmlns:xd=\"http://schemas.microsoft.com/office/infopath/2003\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\"/>";
    private static String transformedXSN;


    @BeforeClass
    public static void setUp() throws Exception {
        transformedXSN = convert(infopathZip);
    }

    private static String convert(String path) throws Exception {

        LinkedMultiValueMap map = new LinkedMultiValueMap();
        map.add("xsn", getMulipartFile(path));
        DefaultMultipartHttpServletRequest request = new DefaultMultipartHttpServletRequest
                (new MockHttpServletRequest(), map, null);
        new InfopathConverterModuleFormController().onSubmit(request, new MockHttpServletResponse(), null, null);
        return (String) request.getSession().getAttribute("htmlform");
    }

    private static MultipartFile getMulipartFile(String path) throws Exception {
        File input = new File(path);
        InputStream stream = new FileInputStream(input);
        MockMultipartHttpServletRequest multipartHttpServletRequest = new MockMultipartHttpServletRequest();
        String fileName = input.getName();

        multipartHttpServletRequest.addFile(new MockMultipartFile(fileName, stream));
        return multipartHttpServletRequest.getFile(input.getName());
    }

    @Test
    @Verifies(value="Transformation generates a valid XML" ,method="onSubmit(HttpServletRequest, HttpServletResponse, Object, BindException)")
    public void shouldReturnAValidXML() throws Exception {
        XMLUnit.buildControlDocument(transformedXSN);
    }

    @Test
    @Verifies(value="Transformation a page with title enclosed in htmlform tags" ,method="onSubmit(HttpServletRequest, HttpServletResponse, Object, BindException)")
    public void shouldContainPageElementWithTitle() throws Exception {
        assertXpathExists("/htmlform/page[@title='Page1.xsl']", transformedXSN);
    }

    @Test
    @Verifies(value="All element with xd:binding - 'patient/*' must be transformed to a lookup element" ,method="onSubmit(HttpServletRequest, HttpServletResponse, Object, BindException)")
    public void shouldHavePatientTransformation() throws Exception {
        assertXpathExists("//lookup[@expression='patient.personName.givenName']", transformedXSN);
        assertXpathExists("//lookup[@expression='patient.personName.familyName']", transformedXSN);
        assertXpathExists("//lookup[@expression='patient.patientIdentifier.identifier']", transformedXSN);

    }

    @Test
    @Verifies(value="All element with xd:binding - '*patient/*' must not be transformed to a lookup element" ,method="onSubmit(HttpServletRequest, HttpServletResponse, Object, BindException)")
    public void shouldNotTransformBindingsNotStartingWithPatient() throws Exception {
        assertXpathNotExists("//lookup[@expression='']", transformedXSN);
    }

    @Test
    public void shouldNotChangeQuotingInAttributes() throws Exception {
        assertXpathExists("//input[@value='&lt;- Select Provider']", transformedXSN);
    }

    @Test
    public void shouldReturnEmptyHtmlFormForInvalidFiles() throws Exception {
        String transformedXSN = convert("./test/org/openmrs/module/infopathconverter/include/invalidInfopath.txt");
        String expectedEmptyForm = EMPTY_HTML_FORM;
        assertXMLEqual(transformedXSN, expectedEmptyForm);
    }

    @Test
    @Verifies(value="All date picker widget bound to encounter/encounter.encounter_datetime should be transformed to an 'encounterDate'" ,method="onSubmit(HttpServletRequest, HttpServletResponse, Object, BindException)")
    public void shouldTransformEncounterDate() throws Exception {
        assertXpathExists("//encounterDate", transformedXSN);

    }

    @Test
    @Verifies(value="All element bound to encounter/*location should be transformed to an encounterLocation" ,method="onSubmit(HttpServletRequest, HttpServletResponse, Object, BindException)")
    public void shouldTransferEncounterLocation() throws Exception {
        assertXpathExists("//encounterLocation[@order='30,27,28,25,37,38,26,29,1']", transformedXSN);
    }

    @Test
    @Verifies(value="All widget bound to encounter/encounter.provider_id should be transformed to encounterProvider" ,method="onSubmit(HttpServletRequest, HttpServletResponse, Object, BindException)")
    public void shouldTransferEncounterProvider() throws Exception {
        assertXpathExists("//encounterProvider", transformedXSN);
    }

    @Test
    @Verifies(value="All coded observations should be transformed to obs element with conceptId and answerConceptId" ,method="onSubmit(HttpServletRequest, HttpServletResponse, Object, BindException)")
    public void shouldTransformObservations() throws Exception {
        assertXpathExists("//obs[@conceptId='3389' and @answerConceptId='1065']", transformedXSN);
        assertXpathExists("//obs[@conceptId='3301' and @answerConceptId='1065,1066']", transformedXSN);
        assertXpathExists("//obs[@conceptId='3139' and @answerConceptId='3138,3135,3136,6246,3137,3114,3999']", transformedXSN);
    }

    @Test
    @Verifies(value="No AnswerConceptIds should exist with an empty value" ,method="onSubmit(HttpServletRequest, HttpServletResponse, Object, BindException)")    
    public void shouldNotContainTheAnswerIdAttributeForNotSpecifiedObs() throws Exception {
        assertXpathNotExists("//obs[@answerConceptId='']", transformedXSN);
    }

    @Test
    @Verifies(value="No OpenMRSConcept of type 'ZZ' should be considered" ,method="onSubmit(HttpServletRequest, HttpServletResponse, Object, BindException)")
    public void shouldEnsureThatOpenMRSConceptOfDatatypeZZAreNotPickedUp() throws Exception {
        assertXpathExists("//obs[@conceptId='1119']", transformedXSN);
    }

    @Test
    @Verifies(value="All widgets with obs/* binding and multiple - 1 should be transformed as individual obs element (multi-select coded observation)" ,method="onSubmit(HttpServletRequest, HttpServletResponse, Object, BindException)")        
    public void shouldEnsureThatIndividualTransformationsAreDoneWhenMultipleIsOne() throws Exception {
        assertXpathExists("//obs[@conceptId='1119'and @answerConceptId='460']", transformedXSN);
        assertXpathExists("//obs[@conceptId='1119'and @answerConceptId='215']", transformedXSN);
        assertXpathExists("//obs[@conceptId='1119'and @answerConceptId='161']", transformedXSN);
    }

    @Test
    @Verifies(value="All widgets with obs/* binding and type-radio should be transformed to obs element with all values as a comma seperated string assigned to its answerConceptId" ,method="onSubmit(HttpServletRequest, HttpServletResponse, Object, BindException)")            
    public void shouldConvertRadioElementToCommaSeparatedAnswers() throws Exception {
        assertXpathExists("//obs[@conceptId='3139'and @answerConceptId='3138,3135,3136,6246,3137,3114,3999']", transformedXSN);

    }

    @Test
    @Verifies(value="All widgets bound to obs/* with a boolean class should be transformed to a checkbox for boolean observation" ,method="onSubmit(HttpServletRequest, HttpServletResponse, Object, BindException)")            
    public void shouldConvertCheckboxToBooleanCodedObservation() throws Exception {
        assertXpathExists("//obs[@conceptId='6208' and @style='checkbox']", transformedXSN);
    }


    @Test
    @Verifies(value="All widgets that are bound to obs/* with a type - textarea is converted to a text field for text observation" ,method="onSubmit(HttpServletRequest, HttpServletResponse, Object, BindException)")            
    public void shouldConvertTextBoxToTextObservation() throws Exception {
        assertXpathExists("//obs[@conceptId='3221' and @style='textarea']", transformedXSN);
    }

    @Test
    @Verifies(value="All widgets that are bound to obs/* with a type - numeric is converted to a text field for numeric observation" ,method="onSubmit(HttpServletRequest, HttpServletResponse, Object, BindException)")            
    public void shouldConvertTextBoxToNumericObservation() throws Exception {
        assertXpathExists("//obs[@conceptId='3221']", transformedXSN);
    }

    @Test
    @Verifies(value="All widgets that are bound to obs/* with a type - date is converted to a text field for date observation" ,method="onSubmit(HttpServletRequest, HttpServletResponse, Object, BindException)")                
    public void shouldConvertTextBoxToDateObservation() throws Exception {
        assertXpathExists("//obs[@conceptId='3221']", transformedXSN);
    }

    @Test
    @Verifies(value="The control corresponding to the submit button should be transformed to a submit element" ,method="onSubmit(HttpServletRequest, HttpServletResponse, Object, BindException)")                
    public void shouldConvertSubmitButton() throws Exception {
        assertXpathExists("//submit", transformedXSN);
    }

}
