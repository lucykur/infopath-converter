package org.openmrs.module.infopathconverter.web.controller;

import javax.xml.namespace.NamespaceContext;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: lkurian
 * Date: Jun 29, 2010
 * Time: 2:56:34 PM
 * To change this template use File | Settings | File Templates.
 */
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
		// TODO Auto-generated method stub
		return null;
	}

	public Iterator getPrefixes(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
