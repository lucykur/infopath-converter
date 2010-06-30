/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.infopathconverter.web.controller;

import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.zip.ZipInputStream;

@Controller
@RequestMapping("module/infopathconverter/infopathconvertermoduleLink.form")
public class InfopathConverterModuleFormController {

    @RequestMapping(method = RequestMethod.GET)
    public void showForm() {

    }

    @RequestMapping(method = RequestMethod.POST)
    public void convert(ModelMap map, HttpSession session, @RequestParam(value = "xsn", required = true) MultipartFile file) throws IOException {
        ZipInputStream inputStream = new ZipInputStream(file.getInputStream());        
        Infopath infopath = new Infopath(inputStream);
        try {
            map.addAttribute("htmlform", infopath.toHTMLForm());
        } catch (Exception e) {
            session.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "infopathcoverter.parse.failure");
            session.setAttribute(WebConstants.OPENMRS_ERROR_ARGS, e.getMessage());

        }
    }

}
