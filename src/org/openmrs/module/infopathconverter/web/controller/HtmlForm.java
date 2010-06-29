package org.openmrs.module.infopathconverter.web.controller;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: lkurian
 * Date: Jun 28, 2010
 * Time: 2:52:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class HtmlForm {
    private List<String> pages;

    public HtmlForm() {
        pages = new ArrayList<String>();
    }

    public String toString() {
        StringBuffer xml = new StringBuffer();
        xml.append("<htmlform>");
        for (String page : pages) {
            xml.append(page);
        }
        xml.append("</htmlform>");
        return xml.toString();
    }

    public void addPage(String page) {
        pages.add(page);
    }
}
