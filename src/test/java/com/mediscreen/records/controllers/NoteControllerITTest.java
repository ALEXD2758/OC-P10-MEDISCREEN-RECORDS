package com.mediscreen.records.controllers;

import com.mediscreen.records.model.NoteModel;
import com.mediscreen.records.service.NoteService;
import com.mediscreen.records.service.PatientWebClientService;
import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
public class NoteControllerITTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webContext;

    @Autowired
    private NoteService noteService;

    @Autowired
    private PatientWebClientService patientWebClientService;

    @Before
    public void setupMockmvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
    }

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

    @Test
    public void getRequestNoteListWithExistentIdShouldReturnNoteListView() throws Exception {
        //1. Setup
        List<NoteModel> noteslist = new ArrayList<>();
        noteslist.add(noteModel1());
        noteslist.add(noteModel3());

        //2. Act
        doReturn(true)
                .when(noteService)
                .getAllNotesByPatientId(1);

        mockMvc.perform(get("/note/list/{patientId}", "1"))
        //3. Assert
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("note/list/{patientId}"))
                .andExpect(model().attributeExists("notes"))
                .andReturn();

        assertTrue(noteslist.get(0).getComment().equals("Diabete Type A"));
        assertTrue(noteslist.get(1).getComment().equals("Diabete Type C"));
    }

    @Test
    public void getRequestNoteAddWithExistentIdShouldReturnNoteAddView() throws Exception {
        //1. Setup
        List<NoteModel> noteslist = new ArrayList<>();
        noteslist.add(noteModel1());
        noteslist.add(noteModel3());

        //2. Act
        doReturn(true)
                .when(patientWebClientService)
                .checkPatientIdExist(1);

        mockMvc.perform(get("/note/add/{patientId}", "1"))
        //3. Assert
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("note/add"))
                .andExpect(model().attributeExists("note"))
                .andReturn();
    }

    @Test
    public void getRequestNoteAddWithNonExistentIdShouldRedirectToPatientListWithErrorPatientIdMessage() throws Exception {
        //1. Setup
        List<NoteModel> noteslist = new ArrayList<>();
        noteslist.add(noteModel1());
        noteslist.add(noteModel3());

        //2. Act
        doReturn(false)
                .when(patientWebClientService)
                .checkPatientIdExist(1);

        mockMvc.perform(get("/note/add/{patientId}", "1")
                .flashAttr("ErrorPatientIdMessage", "Patient ID doesn't exist"))
        //3. Assert
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/patient/list"))
                .andExpect(flash().attributeExists("ErrorPatientIdMessage"))
                .andReturn();
    }

    @Test
    public void postRequestPatientPostUpdateWithExistentIdShouldReturnSuccess() throws Exception {
        //1. Setup
        //2. Act
        doReturn(noteModel1())
                .when(noteService)
                .saveNote(noteModel1());

        mockMvc.perform(post("/patient/update/{id}", "1")
                .flashAttr("successSaveMessage", "Your patient was successfully saved")
                .param("id", "1")
                .param("patientId", "1")
                .param("creationDateTime", noteModel1().getCreationDateTime().toString())
                .param("comment", "Diabete Type A"))
        //3. Assert
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/note/list/{patientId}"))
                .andExpect(flash().attributeExists("successSaveMessage"))
                .andReturn();
    }

    @Test
    public void postRequestPatientPostUpdateWithMissingCommentShouldReturnNoteAddView() throws Exception {
        //1. Arrange
        noteModel1().setComment("");
        //2. Act
        doReturn(noteModel1())
                .when(noteService)
                .saveNote(noteModel1());

        mockMvc.perform(post("/patient/update/{id}", "1")
                .param("id", "1")
                .param("patientId", "1")
                .param("creationDateTime", noteModel1().getCreationDateTime().toString())
                .param("comment", ""))
        //3. Assert
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("/note/add"))
                .andReturn();
    }
}