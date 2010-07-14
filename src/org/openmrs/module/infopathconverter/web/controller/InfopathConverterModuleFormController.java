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
import org.openmrs.web.WebConstants;
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

    /**
	 * Handles the transformation of the input file(*.zip) into an HTML file
	 * as per the rules stated on POST
	 *
	 * @should The HTML form should have a page with its title set enclosed in htmlform tags
	 * @should The HTML form should be a valid XML
	 * @should All element with xd:binding - 'patient/*' must be transformed to a 'lookup' element
	 * @should All element with xd:binding - '*patient/*' must not be transformed to a lookup element
	 * @should All date picker widget bound to encounter/encounter.encounter_datetime should be transformed to an 'encounterDate'
	 * @should All element bound to encounter/*location should be transformed to an encounterLocation
	 * @should All widget bound to encounter/encounter.provider_id should be transformed to encounterProvider
	 * @should All coded observations should be transformed to obs element with conceptId and answerConceptId
	 * @should No AnswerConceptIds should exist with an empty value
	 * @should No OpenMRSConcept of type 'ZZ' should be considered
	 * @should All widgets with obs/* binding and multiple - 1 should be transformed as individual obs element (multi-select coded observation)
	 * @should All widgets with obs/* binding and type-radio should be transformed to obs element with all values as a comma seperated string assigned to its answerConceptId
	 * @should All widgets bound to obs/* with a boolean class should be transformed to a checkbox for boolean observation
	 * @should All widgets that are bound to obs/* with a type - textarea is converted to a text field for text observation
	 * @should All widgets that are bound to obs/* with a type - numeric is converted to a text field for numeric observation
	 * @should All widgets that are bound to obs/* with a type - date is converted to a text field for date observation
	 * @should The control corresponding to the submit button should be transformed to a submit element
	 */
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        ModelAndView view = new ModelAndView(new RedirectView(getSuccessView()));
        if (request instanceof MultipartHttpServletRequest) {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            MultipartFile xsnFile = multipartRequest.getFile("xsn");
            if (xsnFile != null && !xsnFile.isEmpty()) {
                convert(request.getSession(), xsnFile);
                return view;
            }
        }

        return view;
    }

    /**
	 * Method that does the transformation when the OnSubmit is invoked on POST
	 *
	 * @should perform all the transformations from the XSN to the HTML form
	 */
    private void convert(HttpSession session, MultipartFile file) throws Exception {

        ZipInputStream inputStream = new ZipInputStream(file.getInputStream());
        Infopath infopath = new Infopath(inputStream);
        try {
        session.setAttribute("htmlform", infopath.toHTMLForm());
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "infopathcoverter.parse.failure");
            session.setAttribute(WebConstants.OPENMRS_ERROR_ARGS, e.getMessage());
        }
    }


    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        return "";
    }
}
