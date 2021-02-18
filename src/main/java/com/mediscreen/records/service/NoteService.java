package com.mediscreen.records.service;

import com.mediscreen.records.model.NoteModel;
import com.mediscreen.records.repository.NoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final Logger logger = LoggerFactory.getLogger(NoteService.class);

    private final NoteRepository noteRep;

    public NoteService(NoteRepository noteRep) {
        this.noteRep = noteRep;
    }

    /**
     * Get a list of all notes of a specific patient id
     *
     * @return list of NoteModel containing all note models
     */
    public List<NoteModel> getAllNotesByPatientId(int patientId) {
        return noteRep.findAllByPatientId(patientId);
    }

    /**
     * Check if a patient Id exists
     * @param patientId the patient ID
     * @return true if patient ID already exists, false if patient ID doesn't exist
     */
    public boolean checkIdExists(int patientId) {
        return noteRep.existsByPatientId(patientId);
    }

    /**
     * Set a new NoteModel from data inside the request, to the NoteModel
     * Save a new note in the DB
     *
     * @param note the NoteModel to save
     * @return boolean, true if the note was saved, false if not saved
     */
    public void saveNote(NoteModel note) {
        noteRep.save(note);
    }
}