package com.mediscreen.records.controller;

import com.mediscreen.records.service.PatientWebClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PatientController {

    private final Logger logger = LoggerFactory.getLogger(PatientController.class);

    @Autowired
    PatientWebClientService patientWebClientService;

    /**
     * Get the ModelAndView patient/list
     * Adds attribute "patients" to the model, containing all patients available in DB
     *
     * @param model Model Interface, to add attributes to it
     * @return a string to the address "patient/list", returning the associated view
     * with attribute
     */
    @GetMapping("/patient/list")
    public String patientList(Model model) {
        model.addAttribute("patients", patientWebClientService.getListPatients());
        logger.info("GET /patient/list : OK");
        return "patient/list";
    }
}