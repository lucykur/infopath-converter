package org.openmrs.module.infopathconverter.xmlutils;

import javax.xml.namespace.NamespaceContext;
import java.util.Iterator;


public class XDNamespaceContext implements NamespaceContext {

    public String getNamespaceURI(String prefix) {
        if (prefix.equals("xd")) {
            return "http://schemas.microsoft.com/office/infopath/2003";
        } else if (prefix.equals("xsl")) {
            return "http://www.w3.org/1999/XSL/Transform";
        }
        return null;
    }

    public String getPrefix(String arg0) {
        return null;
    }

    public Iterator getPrefixes(String arg0) {
        return null;
    }

}
