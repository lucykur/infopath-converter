package org.openmrs.module.infopathconverter.rules;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.Map;

public abstract class Rule {
    protected Document document;
    private Map<String, Object> expressions;

    public Rule(Document document) {
        this.document = document;
        this.expressions = new HashMap<String, Object>();
    }

    public abstract void apply(Nodes nodes) throws Exception;

    protected void addExpression(String key, Object value) {
        expressions.put(key, value);
    }

    private Object getExpression(String key) {
        return expressions.get(key);
    }

    protected String getExpressionAsString(String key) {
        return (String) getExpression(key);
    }


    protected XmlNode getExpressionAsNode(String key) {
        return (XmlNode) getExpression(key);
    }

    protected String searchExpression(String binding) {
        for (String encounterName : expressions.keySet()) {
            if (binding.contains(encounterName)) {
                return getExpressionAsString(encounterName);
            }
        }
        return null;
    }
}
