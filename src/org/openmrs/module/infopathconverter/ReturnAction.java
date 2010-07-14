package org.openmrs.module.infopathconverter;

import org.openmrs.module.infopathconverter.XmlNode;

public abstract class ReturnAction<T> {
    abstract public T execute(XmlNode node);
}
