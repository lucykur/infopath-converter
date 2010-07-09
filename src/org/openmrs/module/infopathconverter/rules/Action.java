package org.openmrs.module.infopathconverter.rules;

public abstract class Action<T> {
    public abstract void execute(T node) throws Exception;
}
