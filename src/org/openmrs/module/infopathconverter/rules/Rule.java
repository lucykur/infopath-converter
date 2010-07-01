package org.openmrs.module.infopathconverter.rules;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public interface Rule {
    void apply(Document page, NodeList nodes);
}
