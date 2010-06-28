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

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("module/infopathconverter/infopathconvertermoduleLink.form")
public class InfopathConverterModuleFormController {

    @RequestMapping(method = RequestMethod.GET)
    public void showForm() {

    }

    @RequestMapping(method = RequestMethod.POST)
    public void convert(ModelMap map, @RequestParam(value = "xsn", required = true) MultipartFile file) throws IOException {
//        Infopath infopath = new Infopath(file);
//        Rules rules = infopath.parse();
//        HtmlFormBuilder builder = new HtmlFormBuilder(rules);
//        HtmlForm htmlForm = builder.build();
//        map.put("htmlform", htmlForm.toString());

    }
}
