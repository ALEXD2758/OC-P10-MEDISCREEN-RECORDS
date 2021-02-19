package com.mediscreen.records.services;

import com.mediscreen.records.model.AddressModel;
import com.mediscreen.records.model.NoteModel;
import com.mediscreen.records.repository.NoteRepository;
import com.mediscreen.records.service.NoteService;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NoteServiceUTests {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private NoteService noteService;

    @Autowired
    private NoteRepository noteRepository;

    public NoteModel noteModel1() {
        LocalDateTime date = new LocalDateTime(2021/01/01);

        NoteModel noteModel1 = new NoteModel();
        noteModel1.setPatientId(1);
        noteModel1.setCreationDateTime(date);
        noteModel1.setComment("Diabete Type A");
        return noteModel1;
    }

    public NoteModel noteModel2() {
        LocalDateTime date = new LocalDateTime(2021/02/02);

        NoteModel noteModel2 = new NoteModel();
        noteModel2.setPatientId(2);
        noteModel2.setCreationDateTime(date);
        noteModel2.setComment("Diabete Type B");
        return noteModel2;
    }

    public NoteModel noteModel3() {
        LocalDateTime date = new LocalDateTime(2021/02/04);

        NoteModel noteModel3 = new NoteModel();
        noteModel3.setPatientId(1);
        noteModel3.setCreationDateTime(date);
        noteModel3.setComment("Diabete Type C");
        return noteModel3;
    }

    public NoteModel noteModel4() {
        LocalDateTime date = new LocalDateTime(2021/02/05);

        NoteModel noteModel4 = new NoteModel();
        noteModel4.setPatientId(2);
        noteModel4.setCreationDateTime(date);
        noteModel4.setComment("Diabete Type D");
        return noteModel4;
    }

    @Before
    public void saveNotesToDbBeforeTests() throws SQLException {
     //   ScriptUtils.executeSqlScript(dataSource.getConnection(), new FileSystemResource("src/test/resources" +
     //           "/db_test_scriptV2.sql"));
        noteService.saveNote(noteModel1());
        noteService.saveNote(noteModel2());
        noteService.saveNote(noteModel3());
        noteService.saveNote(noteModel4());
    }

    @After
    public void deleteAllNotesAfterTests() {
        noteRepository.deleteAll();
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
    public void checkExistentPatientByIdShouldReturnTrue() {
        //ARRANGE
        int id = 1;

        //ACT
        boolean existentPatientById = noteService.checkIdExists(id);

        //ASSERT
        Assert.assertEquals(true, existentPatientById);
    }

    @Test
    public void savePatientShouldSaveANewPatient() {
        //ARRANGE
        LocalDateTime date = new LocalDateTime(2021/01/06);

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
}