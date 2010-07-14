package org.openmrs.module.infopathconverter;

public abstract class ReturnAction<T> {
    abstract public T execute(XmlNode node);
}
