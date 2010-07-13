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

import org.openmrs.module.infopathconverter.Infopath;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.zip.ZipInputStream;

public class InfopathConverterModuleFormController extends SimpleFormController {


    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        ModelAndView view = new ModelAndView(new RedirectView(getSuccessView()));
        if (request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile xsnFile = multipartRequest.getFile("xsn");
            if (xsnFile != null && !xsnFile.isEmpty()) {
                convert(request, request.getSession(), xsnFile);
                return view;
            }
        }

        return view;
    }

    public void convert(HttpServletRequest map, HttpSession session, MultipartFile file) throws Exception {

        ZipInputStream inputStream = new ZipInputStream(file.getInputStream());
        Infopath infopath = new Infopath(inputStream);
//        try {        
        session.setAttribute("htmlform", infopath.toHTMLForm());
//        } catch (Exception e) {
//            e.printStackTrace();
//            session.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "infopathcoverter.parse.failure");
//            session.setAttribute(WebConstants.OPENMRS_ERROR_ARGS, e.getMessage());
//        }
    }


    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        return "";
    }
}
