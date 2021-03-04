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
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class NoteController {

    private final Logger logger = LoggerFactory.getLogger(NoteController.class);

    @Autowired
    NoteService noteService;

    @Autowired
    PatientWebClientService patientWebClientService;


    /**
     * HTTP GET request for deleting all notes for all patients
     *
     */
    @GetMapping("/getNoteList")
    public @ResponseBody List<NoteModel> getNoteList(Integer patientId) {
        logger.info("GET /getNoteList : OK");
        return noteService.getAllNotesByPatientId(patientId);
    }

    /**
     * HTTP GET request for deleting all notes for all patients
     *
     */
    @DeleteMapping("/notes/delete")
    public String deleteAllNotes() {
        noteService.deleteAllNotes();
        logger.info("DELETE /notes/delete : OK");
        return "redirect:/patient/list";
    }

    /**
     * HTTP GET request the ModelAndView note/list
     * Adds attributes "notes" and notePatientId to the model, containing all notes available in DB for that patient ID
     *
     * @param patientId the int of the patient id chosen
     * @param model Model Interface, to add attributes to it
     * @return a string to the address "note/list" or patient/list, returning the associated view
     * with attribute
     */
    @GetMapping("/note/list/{patientId}")
    public String notesList(@PathVariable("patientId") int patientId, Model model, RedirectAttributes ra) {
        if (patientWebClientService.checkPatientIdExist(patientId) == false) {
            ra.addFlashAttribute("ErrorPatientIdMessage", "Patient ID doesn't exist");
            logger.info("GET /note/add : Non existent id");
            return "redirect:/patient/list";
        }
        model.addAttribute("notes", noteService.getAllNotesByPatientId(patientId));
        model.addAttribute("notePatientId", patientId);
        logger.info("GET /note/list : OK");
        return "note/list";
    }

    /**
     * HTTP GET request to the view note/add with the chosen patient in a model attribute
     * with the associated data of the chosen ID
     *
     * @param patientId the int of the patient id chosen
     * @param model the Model Interface, to add attributes to it
     * @return a string to the view "note/add", returning the associated view
     * with attribute (if no Exception)
     */
    @GetMapping("/note/add/{patientId}")
    public String noteAdd(@PathVariable("patientId") int patientId, final Model model, RedirectAttributes ra) {
        if (patientWebClientService.checkPatientIdExist(patientId) == false) {
            ra.addFlashAttribute("ErrorPatientIdMessage", "Patient ID doesn't exist");
            logger.info("GET /note/add : Non existent id");
            return "redirect:/patient/list";
        }
        NoteModel newNoteModel = new NoteModel();
        newNoteModel.setPatientId(patientId);
        if(!model.containsAttribute("note")) {
            model.addAttribute("note", newNoteModel);
        }
        logger.info("GET /note/add : OK");
        return "note/add";
    }

    /**
     * HTTP POST request to add a new note to the table notes if BindingResult has no errors
     * Set the creationDateTime with a current date-time
     * Add Flash Attribute with success message
     *
     * @param note the NoteModel with annotation @Valid (for the possible constraints)
     * @param result to represent binding results
     * @param patientId the Integer of the patient id chosen
     * @param ra the RedirectAttributes to redirect attributes in redirect
     * @return if successful, a string to the address "patient/list", returning the associated view,
     * with attributes ; if not, get the "note/add" view ; "redirect:/patient/list if patientId does not exist
     */
    @PostMapping("/note/add/validate/{patientId}")
    public String postNoteAdd(@PathVariable("patientId") int patientId,
                                  @Valid @ModelAttribute("note") final NoteModel note,
                            final BindingResult result, final RedirectAttributes ra) {
        if (patientWebClientService.checkPatientIdExist(patientId) == false) {
            ra.addFlashAttribute("ErrorPatientIdMessage", "Patient ID doesn't exist");
            logger.info("GET /note/add : Non existent id");
            return "redirect:/patient/list";
        }
        if (!result.hasErrors()) {
            note.setCreationDateTime(LocalDateTime.now());
            noteService.saveNote(note);
            ra.addFlashAttribute("successSaveMessage", "Your note was successfully added");
            logger.info("POST /note/add : OK");
            return "redirect:/note/list/{patientId}";
        }
        if (result.hasErrors()) {
            ra.addFlashAttribute("org.springframework.validation.BindingResult.note", result);
            ra.addFlashAttribute("note", note);
            logger.info("POST /note/add : NOK");
        }
        return "redirect:/note/add/{patientId}";
    }

    /**
     * DELETE HTTP Request : Delete existing note from the table notes
     * Add Flash Attribute with success message
     *
     * @param id the String of the ID of the note chosen
     * @param ra the RedirectAttributes to redirect attributes in redirect
     * @return a string to the address "redirect:/patient/list", returning the associated view,
     * with attributes
     */
    @DeleteMapping("/note/delete/{id}")
    public String noteDelete(@PathVariable("id") String id, RedirectAttributes ra) {
        if (noteService.checkIdExists(id) == false) {
            ra.addFlashAttribute("ErrorNoteIdMessage", "ID doesn't exist");
            logger.info("GET /note/delete : Non existent note id");
            return "redirect:/note/list";
        }
        noteService.deleteNotePatient(id);
        ra.addFlashAttribute("successDeleteMessage", "This note was successfully deleted");
        logger.info("/note/delete : OK");

        return "redirect:/patient/list";
    }
}