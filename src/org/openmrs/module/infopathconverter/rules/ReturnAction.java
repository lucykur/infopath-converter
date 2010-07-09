package org.openmrs.module.infopathconverter.rules;

public abstract class ReturnAction<T> {
    abstract public T execute(XmlNode node); 
}
