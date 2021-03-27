package com.mediscreen.records.services;

import com.mediscreen.records.model.NoteModel;
import com.mediscreen.records.repository.NoteRepository;
import com.mediscreen.records.service.NoteService;
import java.time.LocalDateTime;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NoteServiceUTests {

    @Autowired
    private NoteService noteService;

    @Autowired
    private NoteRepository noteRepository;

    public NoteModel noteModel1() {
        LocalDateTime date = LocalDateTime.of(2021,01,01,13,45,30);

        NoteModel noteModel1 = new NoteModel();
        noteModel1.setId("1245567");
        noteModel1.setPatientId(1);
        noteModel1.setCreationDateTime(date);
        noteModel1.setComment("Diabete Type A");
        return noteModel1;
    }

    public NoteModel noteModel2() {
        LocalDateTime date = LocalDateTime.of(2021,02,02,13,45,30);

        NoteModel noteModel2 = new NoteModel();
        noteModel2.setId("1245568");
        noteModel2.setPatientId(2);
        noteModel2.setCreationDateTime(date);
        noteModel2.setComment("Diabete Type B");
        return noteModel2;
    }

    public NoteModel noteModel3() {
        LocalDateTime date = LocalDateTime.of(2021,02,04,13,45,30);

        NoteModel noteModel3 = new NoteModel();
        noteModel3.setId("1245569");
        noteModel3.setPatientId(1);
        noteModel3.setCreationDateTime(date);
        noteModel3.setComment("Diabete Type C");
        return noteModel3;
    }

    public NoteModel noteModel4() {
        LocalDateTime date = LocalDateTime.of(2021,02,05,13,45,30);

        NoteModel noteModel4 = new NoteModel();
        noteModel4.setId("1245577");
        noteModel4.setPatientId(2);
        noteModel4.setCreationDateTime(date);
        noteModel4.setComment("Diabete Type D");
        return noteModel4;
    }

    @Before
    public void saveNotesToDbBeforeTests() {
     //   ScriptUtils.executeSqlScript(dataSource.getConnection(), new FileSystemResource("src/test/resources" +
     //           "/db_test_scriptV2.sql"));
        noteService.saveNote(noteModel1());
        noteService.saveNote(noteModel2());
        noteService.saveNote(noteModel3());
        noteService.saveNote(noteModel4());
    }

    @After
    public void deleteAllNotesAfterTests() {
        noteService.deleteAllNotes();
    }

    @Test
    public void getAllNotesByPatientIdShouldReturnAllPatientNotes() {
        //ARRANGE
        int patientId = 1;
        //ACT
        List<NoteModel> listNotes = noteService.getAllNotesByPatientId(1);
        //ASSERT
        Assert.assertTrue(listNotes.size() == 2);
        Assert.assertTrue(listNotes.get(0).getComment().equals("Diabete Type A"));
        Assert.assertTrue(listNotes.get(1).getComment().equals("Diabete Type C"));
    }

    @Test
    public void checkExistentNoteByIdShouldReturnTrue() {
        //ARRANGE
        String id = "1245567";

        //ACT
        boolean existentPatientById = noteService.checkIdExists(id);

        //ASSERT
        Assert.assertEquals(true, existentPatientById);
    }

    @Test
    public void savePatientShouldSaveANewPatient() {
        //ARRANGE
        LocalDateTime date = LocalDateTime.of(2021,01,06,13,45,30);

        NoteModel noteModel5 = new NoteModel();
        noteModel5.setPatientId(5);
        noteModel5.setCreationDateTime(date);
        noteModel5.setComment("Diabete");
        //ACT
        NoteModel noteToSave = noteService.saveNote(noteModel5);

        //ASSERT
        Assert.assertNotNull(noteToSave.getId());
        Assert.assertNotNull(noteToSave.getCreationDateTime());
        Assert.assertEquals("Diabete", noteToSave.getComment());
    }

    @Test
    public void deleteNotePatientByIdShouldAssertFalseWhenCheckIdExistsIsInvoked() {
        //ARRANGE
        String id = "1245567";

        //ACT
        noteService.deleteNotePatient(id);
        boolean existentPatientById = noteService.checkIdExists(id);

        //ASSERT
        Assert.assertEquals(false, existentPatientById);
    }
}