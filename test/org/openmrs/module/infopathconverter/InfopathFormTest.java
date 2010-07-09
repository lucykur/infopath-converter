package org.openmrs.module.infopathconverter;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import org.junit.Assert;
import org.junit.Test;
import org.openmrs.module.infopathconverter.rules.observation.InfopathXsd;
import org.openmrs.module.infopathconverter.rules.observation.TemplateXml;
import org.openmrs.module.infopathconverter.xmlutils.XmlDocumentFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathNotExists;

public class InfopathFormTest {

    @Test
    public void shouldTransformPatient() throws Exception {
        InfopathForm form = new InfopathForm("page1.xsl", SampleTestElements.PATIENT);
        Document transformedXSN = form.toPage(getTemplate(SampleTestElements.OBSERVATION_CODED_XML), getXsd(SampleTestElements.OBSERVATION_CODED_RADIO_XSD));
        assertXpathExists("//lookup[@expression='patient.personName.givenName']", transformedXSN);
        assertXpathExists("//lookup[@expression='patient.personName.familyName']", transformedXSN);
        assertXpathExists("//lookup[@expression='patient.patientIdentifier.identifier']", transformedXSN);

    }

    private TemplateXml getTemplate(String xml) throws Exception {
        return new TemplateXml(XmlDocumentFactory.createXmlDocumentFromStream(new ByteArrayInputStream(xml.getBytes())));
    }

    @Test
    public void shouldNotTransformBindingsNotStartingWithPatient() throws Exception {
        InfopathForm form = new InfopathForm("page1.xsl", SampleTestElements.OTHER);
        Document transformedXSN = form.toPage(getTemplate(SampleTestElements.OBSERVATION_CODED_XML), getXsd(SampleTestElements.OBSERVATION_CODED_RADIO_XSD));
        assertXpathNotExists("//lookup[@expression='']", transformedXSN);
        assertXpathExists("//span", transformedXSN);
    }


    @Test
    public void shouldTransformEncounterDate() throws Exception {

        InfopathForm form = new InfopathForm("encounter", SampleTestElements.ENCOUNTER_DATE);
        Document transformedXSN = form.toPage(getTemplate(SampleTestElements.OBSERVATION_CODED_XML), getXsd(SampleTestElements.OBSERVATION_CODED_RADIO_XSD));
        assertXpathExists("//encounterDate", transformedXSN);
        assertXpathNotExists("//span", transformedXSN);

    }

    @Test
    public void shouldTransferEncounterLocation() throws Exception {
        InfopathForm form = new InfopathForm("encounter location", SampleTestElements.ENCOUNTER_LOCATION);
        Document document = form.toPage(getTemplate(SampleTestElements.OBSERVATION_CODED_XML), getXsd(SampleTestElements.OBSERVATION_CODED_RADIO_XSD));
        assertXpathExists("//encounterLocation[@order='27,30']", document);
    }


    @Test
    public void shouldTransferEncounterProvider() throws Exception {
        InfopathForm form = new InfopathForm("encounter provider", SampleTestElements.ENCOUNTER_PROVIDER);
        Document document = form.toPage(getTemplate(SampleTestElements.OBSERVATION_CODED_XML), getXsd(SampleTestElements.OBSERVATION_CODED_RADIO_XSD));
        assertXpathExists("//encounterProvider", document);
        assertXpathNotExists("//span", document);
    }

    @Test
    public void shouldTransformObservations() throws Exception {
        InfopathForm form = new InfopathForm("obs", SampleTestElements.OBSERVATION_CODED_XSL);
        Document transformedXSN = form.toPage(getTemplate(SampleTestElements.OBSERVATION_CODED_XML), getXsd(SampleTestElements.OBSERVATION_CODED_RADIO_XSD));
        assertXpathExists("//obs[@conceptId='3389' and @answerConceptId='1065']", transformedXSN);
    }

    @Test
    public void shouldNotContainTheAnswerIdAttributeForNotSpecifiedObs() throws Exception {
        InfopathForm form = new InfopathForm("obs", SampleTestElements.OBSERVATION_CODED_NON_SPECIFIED_XSL);
        Document transformedXSN = form.toPage(getTemplate(SampleTestElements.OBSERVATION_CODED_NON_SPECIFIED_XML), getXsd(SampleTestElements.OBSERVATION_CODED_RADIO_XSD));
        assertXpathExists("//obs[@conceptId='3301']", transformedXSN);
        assertXpathNotExists("//obs[@answerConceptId]", transformedXSN);
    }

    @Test
    public void shouldEnsureThatOpenMRSConceptOfDatatypeZZAreNotPickedUp() throws Exception {
        InfopathForm form = new InfopathForm("obs", SampleTestElements.OBSERVATION_CODED_XSL_TYPE_ZZ);
        Document transformedXSN = form.toPage(getTemplate(SampleTestElements.OBSERVATION_CODED_XML_TYPE_ZZ), getXsd(SampleTestElements.OBSERVATION_CODED_RADIO_XSD));
        assertXpathExists("//obs[@conceptId='1119']", transformedXSN);

    }

    @Test
    public void shouldEnsureThatIndividualTransformationsAreDoneWhenMultipleIsOne() throws Exception {
        InfopathForm form = new InfopathForm("obs", SampleTestElements.OBSERVATION_CODED_MULTIPLE_XSL);
        Document transformedXSN = form.toPage(getTemplate(SampleTestElements.OBSERVATION_CODED_MULTIPLE_XML), getXsd(SampleTestElements.OBSERVATION_CODED_RADIO_XSD));
        assertXpathExists("//obs[@conceptId='1119'and @answerConceptId='460']", transformedXSN);
        assertXpathExists("//obs[@conceptId='1119'and @answerConceptId='215']", transformedXSN);
        assertXpathExists("//obs[@conceptId='1119'and @answerConceptId='161']", transformedXSN);

    }

    @Test
    public void shouldConvertRadioElementToCommaSeparatedAnswers() throws Exception {
        InfopathForm form = new InfopathForm("obs", SampleTestElements.OBSERVATION_CODED_RADIO_XSL);
        Document transformedXSN = form.toPage(getTemplate(SampleTestElements.OBSERVATION_CODED_RADIO_XML), getXsd(SampleTestElements.OBSERVATION_CODED_RADIO_XSD));
        assertXpathExists("//obs[@conceptId='3139'and @answerConceptId='3138,3135,3136,6246,3137,3114,3999']", transformedXSN);

    }

    @Test
    public void shouldConvertCheckboxToBooleanCodedObservation() throws Exception {
        InfopathForm form = new InfopathForm("obs", SampleTestElements.OBSERVATION_CODED_BIT_XSL);
        Document transformedXSN = form.toPage(getTemplate(SampleTestElements.OBSERVATION_CODED_BIT_XML), getXsd(SampleTestElements.OBSERVATION_CODED_RADIO_XSD));
        assertXpathExists("//obs[@conceptId='6208' and @style='checkbox']", transformedXSN);

    }


    @Test
    public void shouldConvertTextBoxToTextObservation() throws Exception {
        InfopathForm form = new InfopathForm("obs", SampleTestElements.OBSERVATION_CODED_TEXT_XSL);
        Document transformedXSN = form.toPage(getTemplate(SampleTestElements.OBSERVATION_CODED_TEXT_XML), getXsd(SampleTestElements.OBSERVATION_CODED_RADIO_XSD));
        assertXpathExists("//obs[@conceptId='3221' and @style='textarea']", transformedXSN);

    }

    @Test
    public void shouldConvertTextBoxToNumericObservation() throws Exception {
        InfopathForm form = new InfopathForm("obs", SampleTestElements.OBSERVATION_CODED_TEXT_XSL);
        Document transformedXSN = form.toPage(getTemplate(SampleTestElements.OBSERVATION_CODED_NUMERIC_XML), getXsd(SampleTestElements.OBSERVATION_CODED_RADIO_XSD));
        assertXpathExists("//obs[@conceptId='3221']", transformedXSN);

    }

    @Test
    public void shouldConvertTextBoxToDateObservation() throws Exception {
        InfopathForm form = new InfopathForm("obs", SampleTestElements.OBSERVATION_CODED_TEXT_XSL);
        Document transformedXSN = form.toPage(getTemplate(SampleTestElements.OBSERVATION_CODED_DATE_XML), getXsd(SampleTestElements.OBSERVATION_CODED_RADIO_XSD));
        assertXpathExists("//obs[@conceptId='3221']", transformedXSN);

    }

    @Test
    public void shouldConvertSubmitButton() throws Exception {
        InfopathForm form = new InfopathForm("obs", SampleTestElements.SUBMIT_XSL);
        Document transformedXSN = form.toPage(getTemplate(SampleTestElements.OBSERVATION_CODED_DATE_XML), getXsd(SampleTestElements.OBSERVATION_CODED_RADIO_XSD));
        assertXpathExists("//submit", transformedXSN);

    }

    private InfopathXsd getXsd(String xsd) throws IOException, SAXException, ParserConfigurationException {
        return new InfopathXsd(XmlDocumentFactory.createXmlDocumentFromStream(new ByteArrayInputStream(xsd.getBytes())));
    }

}

