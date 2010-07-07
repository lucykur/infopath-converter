package org.openmrs.module.infopathconverter.rules.observation;

public class OpenMRSConcept {
    public String multiple;
    public String id;
    public String datatype;

    public OpenMRSConcept(String multiple, String id, String datatype) {
        this.multiple = multiple;
        this.id = id;
        this.datatype = datatype;
    }

    public boolean isMultiple() {
        return multiple.equals("0");
    }
}
