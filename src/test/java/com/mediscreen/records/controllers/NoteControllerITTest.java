package com.mediscreen.records.controllers;

import com.mediscreen.records.model.AddressModel;
import com.mediscreen.records.model.NoteModel;
import com.mediscreen.records.model.PatientModel;
import com.mediscreen.records.service.NoteService;
import com.mediscreen.records.service.PatientWebClientService;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
public class NoteControllerITTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webContext;

    @MockBean
    private NoteService noteService;

    @MockBean
    private PatientWebClientService patientWebClientService;

    @Before
    public void setupMockmvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
    }

    public NoteModel noteModel1() {
        LocalDateTime date = LocalDateTime.of(2021,01, 01, 13, 45,30);

        NoteModel noteModel1 = new NoteModel();
        noteModel1.setId("1245567");
        noteModel1.setPatientId(1);
        noteModel1.setCreationDateTime(date);
        noteModel1.setComment("Diabete Type A");
        return noteModel1;
    }

    public NoteModel noteModel2() {
        LocalDateTime date = LocalDateTime.of(2021,02,02, 13,45,30);

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

    public AddressModel addressModel1() {
        AddressModel addressModel1 = new AddressModel();
        addressModel1.setStreet("StreetTest1");
        addressModel1.setCity("CityTest1");
        addressModel1.setPostcode("112345");
        addressModel1.setDistrict("DistrictTest1");
        addressModel1.setState("StateTest1");
        addressModel1.setCountry("CountryTest1");
        return addressModel1;
    }

    public PatientModel patientModel1() {

        LocalDate date = new LocalDate(2020, 01,01);
        PatientModel patientModel1 = new PatientModel();
        patientModel1.setGivenName("John");
        patientModel1.setFamilyName("Boyd");
        patientModel1.setBirthdate(date);
        patientModel1.setGender("MALE");
        patientModel1.setAddress(addressModel1());
        patientModel1.setEmailAddress("EmailTest1@email.com");
        patientModel1.setPhoneNumber("004678925899");
        return patientModel1;
    }

    @Test
    public void getRequestGetNoteListShouldReturnAllNotesOfAPatientId() throws Exception {
        //1. Setup
        List<NoteModel> notesList = new ArrayList<>();
        notesList.add(noteModel1());
        notesList.add(noteModel3());

        //2. Act
        doReturn(notesList)
                .when(noteService)
                .getAllNotesByPatientId(1);

        MvcResult result = mockMvc.perform(get("/getNoteList")
                .param("patientId", "1"))
                //3. Assert
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("Diabete Type C"));
    }

    @Test
    public void deleteRequestDeleteAllNotesShouldDeleteAllNotes() throws Exception {
        //1. Setup
        List<NoteModel> notesList = new ArrayList<>();
        notesList.add(noteModel1());
        notesList.add(noteModel3());

        //2. Act
        doReturn(notesList)
                .when(noteService)
                .getAllNotesByPatientId(1);

        MvcResult result = mockMvc.perform(delete("/notes/delete"))
                //3. Assert
                .andExpect(status().is3xxRedirection())
                .andReturn();
    }

    @Test
    public void getRequestNoteListWithExistentIdShouldReturnNoteListView() throws Exception {
        //1. Setup
        List<NoteModel> notesList = new ArrayList<>();
        notesList.add(noteModel1());
        notesList.add(noteModel3());

        //2. Act

        doReturn(true)
                .when(patientWebClientService)
                .checkPatientIdExist(1);

        doReturn(notesList)
                .when(noteService)
                .getAllNotesByPatientId(1);

        mockMvc.perform(get("/note/list/{patientId}", "1"))
        //3. Assert
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("note/list"))
                .andExpect(model().attributeExists("notes"))
                .andReturn();

        assertTrue(notesList.get(0).getComment().equals("Diabete Type A"));
        assertTrue(notesList.get(1).getComment().equals("Diabete Type C"));
    }

    @Test
    public void getRequestNoteListWithNonExistentIdShouldReturnPatientListView() throws Exception {
        //1. Setup
        List<NoteModel> notesList = new ArrayList<>();
        notesList.add(noteModel1());
        notesList.add(noteModel3());

        //2. Act

        doReturn(false)
                .when(patientWebClientService)
                .checkPatientIdExist(1);

        doReturn(notesList)
                .when(noteService)
                .getAllNotesByPatientId(1);

        mockMvc.perform(get("/note/list/{patientId}", "1"))
                //3. Assert
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/patient/list"))
                .andReturn();

        assertTrue(notesList.get(0).getComment().equals("Diabete Type A"));
        assertTrue(notesList.get(1).getComment().equals("Diabete Type C"));
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
    public void postRequestPostNoteAddWithExistentIdShouldReturnSuccess() throws Exception {
        //1. Setup
        noteModel1().setCreationDateTime(LocalDateTime.now());
        //2. Act
        doReturn(true)
                .when(patientWebClientService)
                .checkPatientIdExist(1);

        doReturn(noteModel1())
                .when(noteService)
                .saveNote(noteModel1());

        mockMvc.perform(post("/note/add/validate/{patientId}", "1")
                .flashAttr("successSaveMessage", "Your patient was successfully saved")
                .param("id", "1")
                .param("patientId", "1")
                .param("creationDateTime", "2021-02-22T13:45")
                .param("comment", "Diabete Type A"))
        //3. Assert
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/note/list/{patientId}"))
                .andExpect(flash().attributeExists("successSaveMessage"))
                .andReturn();
    }

    @Test
    public void postRequestPostNoteAddWithNonExistentIdShouldReturnSuccess() throws Exception {
        //1. Setup
        noteModel1().setCreationDateTime(LocalDateTime.now());
        //2. Act
        doReturn(false)
                .when(patientWebClientService)
                .checkPatientIdExist(1);

        mockMvc.perform(post("/note/add/validate/{patientId}", "1")
                .flashAttr("successSaveMessage", "Your patient was successfully saved")
                .param("id", "1")
                .param("patientId", "1")
                .param("creationDateTime", "2021-02-22T13:45")
                .param("comment", "Diabete Type A"))
                //3. Assert
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/patient/list"))
                .andExpect(flash().attributeExists("ErrorPatientIdMessage"))
                .andReturn();
    }

    @Test
    public void postRequestPostNoteAddWithMissingCommentShouldReturnNoteAddView() throws Exception {
        //1. Arrange
        noteModel1().setComment("");
        //2. Act
        doReturn(true)
                .when(patientWebClientService)
                .checkPatientIdExist(1);

        doReturn(noteModel1())
                .when(noteService)
                .saveNote(noteModel1());

        mockMvc.perform(post("/note/add/validate/{patientId}", "1")
                .param("id", "1")
                .param("patientId", "1")
                .param("creationDateTime", "2021-02-22T13:45")
                .param("comment", ""))
        //3. Assert
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/note/add/{patientId}"))
                .andReturn();
    }

    @Test
    public void deleteRequestNoteDeleteShouldReturnSuccess() throws Exception {
        //1. Setup
        //2. Act
        doReturn(true)
                .when(noteService)
                .checkIdExists("1245567");

        doNothing()
                .when(noteService)
                .deleteNotePatient("1245567");

        mockMvc.perform(delete("/note/delete/{id}", "1245567")
                .flashAttr("successDeleteMessage", "This note was successfully deleted"))
        //3. Assert
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/patient/list"))
                .andExpect(flash().attributeExists("successDeleteMessage"))
                .andReturn();
    }

    @Test
    public void deleteRequestNoteDeleteWithNonExistentNoteIdShouldReturnNoteListView() throws Exception {
        //1. Setup
        //2. Act
        doReturn(true)
                .when(noteService)
                .checkIdExists("1245567");

        mockMvc.perform(delete("/note/delete/{id}", "12467")
                .flashAttr("ErrorNoteIdMessage", "ID doesn't exist"))
        //3. Assert
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/note/list"))
                .andExpect(flash().attributeExists("ErrorNoteIdMessage"))
                .andReturn();
    }
}