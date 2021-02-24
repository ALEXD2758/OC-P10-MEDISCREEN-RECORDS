package com.mediscreen.records.controllers;

import com.mediscreen.records.model.AddressModel;
import com.mediscreen.records.model.PatientModel;
import com.mediscreen.records.service.PatientWebClientService;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
public class PatientControllerITTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webContext;

    @MockBean
    PatientWebClientService patientWebClientService;

    @Before
    public void setupMockmvc() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webContext).build();
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

    public AddressModel addressModel2() {
        AddressModel addressModel2 = new AddressModel();
        addressModel2.setStreet("StreetTest2");
        addressModel2.setCity("CityTest2");
        addressModel2.setPostcode("212345");
        addressModel2.setDistrict("DistrictTest2");
        addressModel2.setState("StateTest2");
        addressModel2.setCountry("CountryTest2");
        return addressModel2;
    }

    public PatientModel patientModel2() {
        LocalDate date = new LocalDate(2019,01,01);
        PatientModel patientModel2 = new PatientModel();
        patientModel2.setGivenName("Roger");
        patientModel2.setFamilyName("Patterson");
        patientModel2.setBirthdate(date);
        patientModel2.setGender("MALE");
        patientModel2.setAddress(addressModel2());
        patientModel2.setEmailAddress("EmailTest2@email.com");
        patientModel2.setPhoneNumber("004678925899");
        return patientModel2;
    }

    @Test
    public void getRequestNoteListWithExistentIdShouldReturnNoteListView() throws Exception {
        //1. Setup
        List<PatientModel> patientList = new ArrayList<>();
        patientList.add(patientModel1());
        patientList.add(patientModel2());

        //2. Act
        doReturn(patientList)
                .when(patientWebClientService)
                .getListPatients();

        mockMvc.perform(get("/patient/list"))
         //3. Assert
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("patient/list"))
                .andExpect(model().attributeExists("patients"))
                .andReturn();

        Assert.assertTrue(patientList.size() == 2);
        Assert.assertTrue(patientList.get(0).getGivenName().equals("John"));
        Assert.assertTrue(patientList.get(0).getAddress().getStreet().equals("StreetTest1"));
        Assert.assertTrue(patientList.get(1).getGivenName().equals("Roger"));
        Assert.assertTrue(patientList.get(1).getAddress().getStreet().equals("StreetTest2"));
    }
}