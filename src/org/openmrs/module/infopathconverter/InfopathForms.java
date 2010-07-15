package org.openmrs.module.infopathconverter;

import java.util.ArrayList;
import java.util.List;

public class InfopathForms {
    private List<InfopathForm> forms;

    public InfopathForms() {
        forms = new ArrayList<InfopathForm>();
    }

    public void add(InfopathForm infopathForm) {
        forms.add(infopathForm);
    }


    public void forEach(Action<InfopathForm> action) throws Exception {
        for (InfopathForm form : forms) {
            action.execute(form);
        }
    }

    public int length() {
       return forms.size();
    }
}
