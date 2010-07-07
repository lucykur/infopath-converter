package org.openmrs.module.infopathconverter.rules.observation;

import java.util.List;

public class OpenMRSConcept {
    public String multiple;
    public String id;
    public String datatype;
    public List<String> answers;

    public OpenMRSConcept(String multiple, String id, String datatype, List<String> answers) {
        this.multiple = multiple;
        this.id = id;
        this.datatype = datatype;
        this.answers = answers;
    }

    public boolean isNotMultiple() {
        return multiple.equals("0");
    }
}
