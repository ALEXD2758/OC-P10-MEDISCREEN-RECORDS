package com.mediscreen.records.controller;

import com.mediscreen.records.model.NoteModel;
import com.mediscreen.records.service.NoteService;
import com.mediscreen.records.service.PatientWebClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
public class NoteController {

    private final Logger logger = LoggerFactory.getLogger(NoteController.class);

    @Autowired
    NoteService noteService;

    @Autowired
    PatientWebClientService patientWebClientService;

    /**
     * Get the ModelAndView note/list
     * Adds attribute "notes" to the model, containing all notes available in DB for that patient ID
     *
     * @param patientId the Integer of the patient id chosen
     * @param model Model Interface, to add attributes to it
     * @return a string to the address "note/list", returning the associated view
     * with attribute
     */
    @GetMapping("/note/list/{patientId}")
    public String notesList(@PathVariable("patientId") Integer patientId, Model model) {
        model.addAttribute("notes", noteService.getAllNotesByPatientId(patientId));
        logger.info("GET /note/list : OK");
        return "note/list";
    }

    /**
     * Get the view patient/update with the chosen patient in a model attribute
     * with the associated data of the chosen ID
     * Add attribute patient to the model
     *
     * @param patientId the Integer of the patient id chosen
     * @param model the Model Interface, to add attributes to it
     * @return a string to the address "patient/update", returning the associated view
     * with attribute (if no Exception)
     */
    @GetMapping("/note/add/{patientId}")
    public String noteAdd(@PathVariable("patientId") Integer patientId, Model model, RedirectAttributes ra) {
        if (patientWebClientService.checkPatientIdExist(patientId) == false) {
            ra.addFlashAttribute("ErrorPatientIdMessage", "Patient ID doesn't exist");
            logger.info("GET /note/add : Non existent id");
            return "redirect:/patient/list";
        }
        NoteModel newNoteModel = new NoteModel();
        newNoteModel.setPatientId(patientId);
        model.addAttribute("note", newNoteModel);
        logger.info("GET /note/add : OK");
        return "note/add";
    }

    /**
     * Update existing note to the table notes if BindingResult has no errors
     * Add Flash Attribute with success message
     *
     * @param note the NoteModel with annotation @Valid (for the possible constraints)
     * @param result to represent binding results
     * @param patientId the Integer of the patient id chosen
     * @param ra the RedirectAttributes to redirect attributes in redirect
     * @return if successful, a string to the address "patient/list", returning the associated view,
     * with attributes ; if not, get the "note/add" view
     */
    @PostMapping("/note/add/{patientId}")
    public String postNoteAdd(@PathVariable("patientId") Integer patientId,
                                  @Valid @ModelAttribute("note") NoteModel note,
                            BindingResult result, RedirectAttributes ra) {
        if (!result.hasErrors()) {
            noteService.saveNote(note);
            ra.addFlashAttribute("successUpdateMessage", "Your note was successfully added");
            logger.info("POST /note/add : OK");
            return "redirect:/patient/list";
        }
        logger.info("POST /note/add : NOK");
        return "/note/add";
    }
}