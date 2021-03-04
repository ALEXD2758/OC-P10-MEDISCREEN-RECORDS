package com.mediscreen.records.service;

import com.mediscreen.records.model.NoteModel;
import com.mediscreen.records.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRep;

    public NoteService(NoteRepository noteRep) {
        this.noteRep = noteRep;
    }

    /**
     * Get a list of all notes of a specific patient id
     *
     * @param patientId int of the patient ID
     * @return list of NoteModel containing all note models
     */
    public List<NoteModel> getAllNotesByPatientId(int patientId) {
        return noteRep.findAllByPatientId(patientId);
    }

    /**
     * Check if a note Id exists
     * @param id string of the note ID
     * @return true if note ID already exists, false if note ID doesn't exist
     */
    public boolean checkIdExists(String id) {
        return noteRep.existsById(id);
    }

    /**
     * Set a new NoteModel from data inside the request, to the NoteModel
     * Save a new note in the DB
     *
     * @param note the NoteModel to save
     * @return NoteModel saved
     */
    public NoteModel saveNote(NoteModel note) {
        return noteRep.save(note);
    }

    /**
     * Delete all notes from all patients
     */
    public void deleteAllNotes() {
        noteRep.deleteAll();
    }

    /**
     * Delete a specific note by ID, from a specific patient
     * @param id int of the note id
     */
    public void deleteNotePatient(String id) {
        noteRep.deleteById(id);
    }
}