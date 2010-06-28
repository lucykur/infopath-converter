package org.openmrs.module.infopathconverter.web.controller;

import org.junit.Assert;
import org.junit.Test;

import java.util.zip.ZipFile;

/**
 * Created by IntelliJ IDEA.
 * User: lkurian
 * Date: Jun 28, 2010
 * Time: 2:54:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class InfopathTest {

    @Test
    public void shouldExtractFormsInInfopathFile() throws Exception {
        Infopath infopath = new Infopath(new ZipFile("./test/org/openmrs/module/infopathconverter/include/infopath.zip"));
        Assert.assertEquals(1, infopath.forms().size());
    }


}
