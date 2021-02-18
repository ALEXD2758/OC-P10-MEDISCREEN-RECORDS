package com.mediscreen.records.repository;

import com.mediscreen.records.model.NoteModel;
import org.joda.time.LocalDate;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
/*
This Repository Interface extends the MongoRepository to access the MongoDB "records"
 */
public interface NoteRepository extends MongoRepository<NoteModel, Integer> {

    boolean existsByPatientId(int patientId);

    List<NoteModel> findAllByPatientId(int patientId);
}