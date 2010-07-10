package org.openmrs.module.infopathconverter;

import org.openmrs.module.infopathconverter.rules.PatientRule;
import org.openmrs.module.infopathconverter.rules.SubmitButtonRule;
import org.openmrs.module.infopathconverter.rules.encounter.EncounterLocationRule;
import org.openmrs.module.infopathconverter.rules.encounter.EncounterRule;
import org.openmrs.module.infopathconverter.rules.observation.InfopathXsd;
import org.openmrs.module.infopathconverter.rules.observation.ObservationRule;
import org.openmrs.module.infopathconverter.rules.observation.TemplateXml;
import org.w3c.dom.Document;


public class InfopathForm {
    private String formName;
    private String content;

    public InfopathForm(String formName, String content) {
        this.formName = formName;
        this.content = content;

    }

    public Document toPage(TemplateXml templateXml, InfopathXsd xsd) throws Exception {
        XmlDocument page = new XmlDocument();
        page.createPage(formName, new XmlDocument(content).getBody());

        page.addRule("//*[starts-with(@xd:binding,'patient/')]", new PatientRule(page.getDocument()));
        page.addRule("//*[starts-with(@xd:binding,'encounter/encounter.encounter_datetime') or contains(@xd:binding,'encounter/encounter.provider_id')]", new EncounterRule(page.getDocument()));
        page.addRule("//*[starts-with(@xd:binding,'encounter/encounter.location_id')]", new EncounterLocationRule(page.getDocument()));
        page.addRule("//*[starts-with(@xd:binding,'obs/')]", new ObservationRule(page.getDocument(), templateXml, xsd));
        page.addRule("//input[@xd:CtrlId='SubmitButton']", new SubmitButtonRule(page.getDocument()));

        page.applyRules();

        return page.getDocument();
    }


}

    