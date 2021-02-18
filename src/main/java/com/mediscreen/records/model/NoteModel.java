package com.mediscreen.records.model;

import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Document(collection="notes")
public class NoteModel {

    @Id
    private Integer id;

    @Field("patientId")
    private Integer patientId;

    @Field("creationDateTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime creationDateTime;

    @NotNull(message="Note cannot be empty")
    @Field("note")
    private String note;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(LocalDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}