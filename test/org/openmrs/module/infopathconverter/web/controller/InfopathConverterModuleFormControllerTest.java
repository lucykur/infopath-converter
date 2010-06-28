package org.openmrs.module.infopathconverter.web.controller;

import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

/**
 * Created by IntelliJ IDEA.
 * User: lkurian
 * Date: Jun 28, 2010
 * Time: 11:25:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class InfopathConverterModuleFormControllerTest {

    @Test
    public void shouldConvertAnXSNFileToHTMLForm() throws URISyntaxException, IOException {
        ModelMap map = new ModelMap();
        InputStream stream = new FileInputStream("./test/org/openmrs/module/infopathconverter/include/infopath.zip");
        MockMultipartHttpServletRequest multipartHttpServletRequest = new MockMultipartHttpServletRequest();
        multipartHttpServletRequest.addFile(new MockMultipartFile("infopath.zip", stream));
        MultipartFile file = multipartHttpServletRequest.getFile("infopath.zip");
        InfopathConverterModuleFormController controller = new InfopathConverterModuleFormController();
        controller.convert(map, file);
//        Assert.assertTrue(map.containsKey("htmlform"));
    }

    @Test
    public void shouldConvertMultiPartFileToZipFile() throws Exception {

    }
}
