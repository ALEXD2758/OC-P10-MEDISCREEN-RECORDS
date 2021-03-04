package com.mediscreen.records.repository;

import com.mediscreen.records.model.NoteModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
/*
This Repository Interface extends the MongoRepository to access the MongoDB collection "mediscreen"
 */
public interface NoteRepository extends MongoRepository<NoteModel, Integer> {

    boolean existsById(String id);
    void deleteById(String id );
    List<NoteModel> findAllByPatientId(int patientId);
}