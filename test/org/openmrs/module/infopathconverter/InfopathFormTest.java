package org.openmrs.module.infopathconverter;

import org.junit.Test;
import org.w3c.dom.Document;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathNotExists;

public class InfopathFormTest {

    @Test
    public void shouldTransformPatient() throws Exception {
        InfopathForm form = new InfopathForm("page1.xsl", SampleTestElements.PATIENT);
        Document transformedXSN = form.toPage(null);
        assertXpathExists("//lookup[@expression='patient.personName.givenName']", transformedXSN);
        assertXpathExists("//lookup[@expression='patient.personName.familyName']", transformedXSN);
        assertXpathExists("//lookup[@expression='patient.patientIdentifier.identifier']", transformedXSN);

    }

    @Test
    public void shouldNotTransformBindingsNotStartingWithPatient() throws Exception {
        InfopathForm form = new InfopathForm("page1.xsl", SampleTestElements.OTHER);
        Document transformedXSN = form.toPage(null);
        assertXpathNotExists("//lookup[@expression='']", transformedXSN);
        assertXpathExists("//span", transformedXSN);
    }

        
    @Test
    public void shouldTransformEncounterDate() throws Exception {

        InfopathForm form = new InfopathForm("encounter", SampleTestElements.ENCOUNTER_DATE);
        Document transformedXSN = form.toPage(null);
        assertXpathExists("//encounterDate", transformedXSN);
        assertXpathNotExists("//span", transformedXSN);

    }

    @Test
    public void shouldTransferEncounterLocation() throws Exception {
        InfopathForm form = new InfopathForm("encounter location", SampleTestElements.ENCOUNTER_LOCATION);
        Document document = form.toPage(null);
        assertXpathExists("//encounterLocation[@order='27,30']", document);
    }


    @Test
    public void shouldTransferEncounterProvider() throws Exception {
        InfopathForm form = new InfopathForm("encounter provider", SampleTestElements.ENCOUNTER_PROVIDER);
        Document document = form.toPage(null);
        assertXpathExists("//encounterProvider", document);
        assertXpathNotExists("//span", document);
    }

    @Test
    public void shouldTransformObservations() throws Exception {
        InfopathForm form = new InfopathForm("obs", SampleTestElements.OBSERVATION_CODED_XSL);
        Document transformedXSN = form.toPage(SampleTestElements.OBSERVATION_CODED_XML);
        assertXpathExists("//obs[@conceptId='3389' and @answerConceptId='1065']", transformedXSN);
    }

    @Test
    public void shouldNotContainTheAnswerIdAttributeForNotSpecifiedObs() throws Exception {
        InfopathForm form = new InfopathForm("obs", SampleTestElements.OBSERVATION_CODED_NON_SPECIFIED_XSL);
        Document transformedXSN = form.toPage(SampleTestElements.OBSERVATION_CODED_NON_SPECIFIED_XML);
        assertXpathExists("//obs[@conceptId='3301']", transformedXSN);
        assertXpathNotExists("//obs[@answerConceptId]", transformedXSN);
    }

    @Test
    public void shouldEnsureThatOpenMRSConceptOfDatatypeZZAreNotPickedUp() throws Exception {
        InfopathForm form = new InfopathForm("obs", SampleTestElements.OBSERVATION_CODED_XSL_TYPE_ZZ);
        Document transformedXSN = form.toPage(SampleTestElements.OBSERVATION_CODED_XML_TYPE_ZZ);
        assertXpathExists("//obs[@conceptId='1119']", transformedXSN);

    }

    @Test
    public void shouldEnsureThatIndividualTransformationsAreDoneWhenMultipleIsOne() throws Exception {
        InfopathForm form = new InfopathForm("obs", SampleTestElements.OBSERVATION_CODED_MULTIPLE_XSL);
        Document transformedXSN = form.toPage(SampleTestElements.OBSERVATION_CODED_MULTIPLE_XML);
        assertXpathExists("//obs[@conceptId='1119'and @answerConceptId='460']", transformedXSN);
        assertXpathExists("//obs[@conceptId='1119'and @answerConceptId='215']", transformedXSN);
        assertXpathExists("//obs[@conceptId='1119'and @answerConceptId='161']", transformedXSN);

    }

}

