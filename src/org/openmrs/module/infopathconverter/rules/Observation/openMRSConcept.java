package org.openmrs.module.infopathconverter.rules.observation;

public class OpenMRSConcept {
    public String multiple;
    public String openmrs_concept;
    public String openmrs_datatype;

    public OpenMRSConcept(String multiple, String openmrs_concept, String openmrs_datatype) {
        this.multiple = multiple;
        this.openmrs_concept = openmrs_concept;
        this.openmrs_datatype = openmrs_datatype;
    }
}
