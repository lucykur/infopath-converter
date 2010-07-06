package org.openmrs.module.infopathconverter;

import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathExists;
import static org.custommonkey.xmlunit.XMLAssert.assertXpathNotExists;

public class InfopathFormTest {

    @Test
    public void shouldTransformPatient() throws Exception {
        InfopathForm form = new InfopathForm("page1.xsl", SampleTestElements.PATIENT);
        Document transformedXSN = form.toPage();
        assertXpathExists("//lookup[@expression='patient.personName.givenName']", transformedXSN);
        assertXpathExists("//lookup[@expression='patient.personName.familyName']", transformedXSN);
        assertXpathExists("//lookup[@expression='patient.patientIdentifier.identifier']", transformedXSN);

    }

    @Test
    public void shouldNotTransformBindingsNotStartingWithPatient() throws Exception {
        InfopathForm form = new InfopathForm("page1.xsl", SampleTestElements.OTHER);
        Document transformedXSN = form.toPage();
        assertXpathNotExists("//lookup[@expression='']", transformedXSN);
        assertXpathExists("//span", transformedXSN);
    }

        
    @Test
    public void shouldTransformEncounterDate() throws Exception {

        InfopathForm form = new InfopathForm("encounter", SampleTestElements.ENCOUNTER_DATE);
        Document transformedXSN = form.toPage();
        assertXpathExists("//encounterDate", transformedXSN);
        assertXpathNotExists("//span", transformedXSN);

    }

    @Test
    public void shouldTransferEncounterLocation() throws Exception {
        InfopathForm form = new InfopathForm("encounter location", SampleTestElements.ENCOUNTER_LOCATION);
        Document document = form.toPage();
        assertXpathExists("//encounterLocation[@order='27,30']", document);
    }


    @Test
    public void shouldTransferEncounterProvider() throws Exception {
        InfopathForm form = new InfopathForm("encounter provider", SampleTestElements.ENCOUNTER_PROVIDER);
        Document document = form.toPage();
        assertXpathExists("//encounterProvider", document);
        assertXpathNotExists("//span", document);
    }

    @Test
    @Ignore
    public void shouldTransformObservations() throws Exception {
        InfopathForm form = new InfopathForm("obs", SampleTestElements.OBSERVATION_CODED_XSL, SampleTestElements.OBSERVATION_CODED_XML);
        Document transformedXSN = form.toPage();
        assertXpathExists("//obs[@conceptId='3389']", transformedXSN);
    }

}
