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
    @Ignore("To be implemented once we have more clarity")
    public void shouldTransferEncounterLocation() throws Exception {
        InfopathForm form = new InfopathForm("encounter location", SampleTestElements.ENCOUNTER_LOCATION);
        Document document = form.toPage();
        assertXpathExists("//encounterLocation[@order='30,27']", document);
        assertXpathNotExists("//div", document);
    }


    @Test
    public void shouldTransferEncounterProvider() throws Exception {
        InfopathForm form = new InfopathForm("encounter provider", SampleTestElements.ENCOUNTER_PROVIDER);
        Document document = form.toPage();
        assertXpathExists("//encounterProvider", document);
        assertXpathNotExists("//span", document);
    }

    @Test
    public void shouldTransformObservations() throws Exception {
        InfopathForm form = new InfopathForm("obs", SampleTestElements.OBSERVATION);
        Document transformedXSN = form.toPage();
        assertXpathNotExists("//input", transformedXSN);
    }

}
