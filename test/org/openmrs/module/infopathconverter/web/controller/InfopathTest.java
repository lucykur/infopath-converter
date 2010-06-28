package org.openmrs.module.infopathconverter.web.controller;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

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

//        URL resource = this.getClass().getResource("/org/openmrs/module/infopathconverter/include/infopath.zip");
//        InputStream stream = new FileInputStream(resource.getPath());
        InputStream stream = new FileInputStream("/Users/lkurian/projects/infopath-converter/test/org/openmrs/module/infopathconverter/include/infopath.zip");
        MockMultipartHttpServletRequest multipartHttpServletRequest = new MockMultipartHttpServletRequest();
        multipartHttpServletRequest.addFile(new MockMultipartFile("infopath.zip", stream));
        MultipartFile file = multipartHttpServletRequest.getFile("infopath.zip");
        
        Infopath infopath = new Infopath(file);
        Assert.assertEquals(1, infopath.forms().size());
    }

}
