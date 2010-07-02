package org.openmrs.module.infopathconverter.rules.Observation;

public class openMRSConcept {
    public String multiple;
    public String openmrs_concept;
    public String openmrs_datatype;

    public openMRSConcept(String multiple, String openmrs_concept, String openmrs_datatype) {
        this.multiple = multiple;
        this.openmrs_concept = openmrs_concept;
        this.openmrs_datatype = openmrs_datatype;
    }
}
