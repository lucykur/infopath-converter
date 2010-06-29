package org.openmrs.module.infopathconverter.web.controller;

/**
 * Created by IntelliJ IDEA.
 * User: lkurian
 * Date: Jun 28, 2010
 * Time: 3:01:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class InfopathForm {
    private String formName;
    private String content;

    public InfopathForm(String formName, String content) {
        this.formName = formName;
        this.content = content;
    }

    public String toPage() {
        return String.format("<page title='%s'></page>", formName);
    }
}
