/*package com.mediscreen.records.controllers;


import com.mediscreen.records.model.AddressModel;
import com.mediscreen.records.model.NoteModel;
import com.mediscreen.records.repository.GenderEnum;
import com.mediscreen.records.service.AddressService;
import com.mediscreen.records.service.PatientService;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@SpringBootTest
public class PatientControllerITTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webContext;

    @MockBean
    PatientService patientService;

    @MockBean
    AddressService addressService;

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

    public NoteModel patientModel1() {

        LocalDate date = new LocalDate(2020, 01,01);
        NoteModel patientModel1 = new NoteModel();
        patientModel1.setGivenName("John");
        patientModel1.setFamilyName("Boyd");
        patientModel1.setBirthdate(date);
        patientModel1.setGender(GenderEnum.MALE);
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

    public NoteModel patientModel2() {
        LocalDate date = new LocalDate(2019,01,01);
        NoteModel patientModel2 = new NoteModel();
        patientModel2.setGivenName("Roger");
        patientModel2.setFamilyName("Patterson");
        patientModel2.setBirthdate(date);
        patientModel2.setGender(GenderEnum.MALE);
        patientModel2.setAddress(addressModel2());
        patientModel2.setEmailAddress("EmailTest2@email.com");
        patientModel2.setPhoneNumber("004678925899");
        return patientModel2;
    }

 //   @Before
 //   public void savePatientsToDbBeforeTests() throws SQLException {
 //       ScriptUtils.executeSqlScript(dataSource.getConnection(), new FileSystemResource("src/test/resources" +
 //                                                                                                "/db_test_scriptV2" +
 //        ".sql"));
 //       patientRepository.deleteAll();
 //       patientRepository.save(patientModel1());
 //       patientRepository.save(patientModel2());
 //   }


    @Test
    public void getRequestPatientListViewShouldReturnSuccess() throws Exception {
        //1. Setup
        List<NoteModel> patientList = new ArrayList<>();
        patientList.add(patientModel1());

        doReturn(patientList)
                .when(patientService)
                .getAllPatients();

        //2. Act
        mockMvc.perform(get("/patient/list"))

        //3. Assert
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("patient/list"))
                .andExpect(model().attributeExists("patients"))
                .andReturn();
        assertTrue(patientList.get(0).getGivenName().equals("John"));
    }

    @Test
    public void getRequestPatientAddViewShouldReturnSuccess() throws Exception {
        //1. Setup

        //2. Act
        mockMvc.perform(get("/patient/add"))
        //3. Assert
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("patient/add"))
                .andExpect(model().attributeExists("patient"))
                .andReturn();
    }

    @Test
    public void postRequestPatientAddValidateGivenGoodParametersShouldReturnErrorPatientExistentMessage() throws Exception {
        //1. Setup
        //2. Act
        doReturn(true)
                .when(patientService)
                .checkGivenAndFamilyNamesAndBirthDateExist(patientModel1().getGivenName(),
                patientModel1().getFamilyName(), patientModel1().getBirthdate());

        MvcResult mvcResult = mockMvc.perform(post("/patient/add/validate")
                .flashAttr("ErrorPatientExistentMessage", "Patient already exist")
                .param("id", "1")
                .param("givenName", "John")
                .param("familyName", "Boyd")
                .param("birthdate", "2020-01-01")
                .param("gender", "MALE")
                .param("address.street", "StreetTest1")
                .param("address.city", "CityTest1")
                .param("address.postcode", "112345")
                .param("address.district", "DistrictTest1")
                .param("address.state", "StateTest1")
                .param("address.country","CountryTest1")
                .param("emailAddress", "EmailTest1@email.com")
                .param("phoneNumber", "004678925899"))

        //3. Assert
                .andExpect(status().is3xxRedirection())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:/patient/add"))
                .andExpect(flash().attributeExists("ErrorPatientExistentMessage"))
                .andReturn();
    }

    @Test
    public void postRequestPatientAddValidateGivenGoodParametersShouldReturnPatientAddConfirmationView() throws Exception {
        //1. Setup

        List<NoteModel> patientList = new ArrayList<>();
        patientList.add(patientModel1());

        List<AddressModel> addressList = new ArrayList<>();
        addressList.add(addressModel1());

        //2. Act
        doReturn(false)
                .when(patientService)
                .checkGivenAndFamilyNamesAndBirthDateExist(patientModel1().getGivenName(),
                        patientModel1().getFamilyName(), patientModel1().getBirthdate());

        doReturn(addressList)
                .when(addressService)
                .getAllPatientsWithExistentAddress(patientModel1().getAddress().getStreet(),
                        patientModel1().getAddress().getCity(), patientModel1().getAddress().getPostcode(),
                        patientModel1().getAddress().getCountry());

        doReturn(patientList)
                .when(patientService)
                .getAllPatientsByAddress(patientModel1().getAddress().getStreet(),
                        patientModel1().getAddress().getCity(), patientModel1().getAddress().getCountry());

        mockMvc.perform(post("/patient/add/validate")
                .flashAttr("patientListAtAddress", patientList)
                .flashAttr("patientToCreate", patientModel1())
                .param("id", "1")
                .param("givenName", "John")
                .param("familyName", "Boyd")
                .param("birthdate", "2020-01-01")
                .param("gender", "MALE")
                .param("address.street", "StreetTest1")
                .param("address.city", "CityTest1")
                .param("address.postcode", "112345")
                .param("address.district", "DistrictTest1")
                .param("address.state", "StateTest1")
                .param("address.country","CountryTest1")
                .param("emailAddress", "EmailTest1@email.com")
                .param("phoneNumber", "004678925899"))

        //3. Assert
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/patient/add/confirmation"))
                .andExpect(flash().attributeExists("patientListAtAddress", "patientToCreate"))
                .andReturn();
    }

    @Test
    public void postRequestPatientAddValidateGivenGoodParametersShouldSavePatientAndReturnPatientListView() throws Exception {
        //1. Setup
        List<NoteModel> patientList = new ArrayList<>();
        patientList.add(patientModel1());

        List<AddressModel> addressList = new ArrayList<>();

        //2. Act
        doReturn(addressList)
                .when(addressService)
                .getAllPatientsWithExistentAddress(patientModel1().getAddress().getStreet(),
                        patientModel1().getAddress().getCity(), patientModel1().getAddress().getPostcode(),
                        patientModel1().getAddress().getCountry());

        doNothing()
                .when(patientService)
                .savePatient(patientModel1());

        mockMvc.perform(post("/patient/add/validate")
                .flashAttr("successSaveMessage", "Patient was successfully added")
                .param("id", "1")
                .param("givenName", "John")
                .param("familyName", "Boyd")
                .param("birthdate", "2020-01-01")
                .param("gender", "MALE")
                .param("address.street", "StreetTest1")
                .param("address.city", "CityTest1")
                .param("address.postcode", "112345")
                .param("address.district", "DistrictTest1")
                .param("address.state", "StateTest1")
                .param("address.country","CountryTest1")
                .param("emailAddress", "EmailTest1@email.com")
                .param("phoneNumber", "004678925899"))

        //3. Assert
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/patient/list"))
                .andExpect(flash().attributeExists("successSaveMessage"))
                .andReturn();
    }

    @Test
    public void getRequestPatientAddConfirmationShouldReturnPatientConfirmationAddView() throws Exception {
        //1. Setup

        //2. Act
        mockMvc.perform(get("/patient/add/confirmation")
                .flashAttr("patientToCreate", new NoteModel()))
        //3. Assert
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("patient/confirmationAdd"))
                .andReturn();
    }

    @Test
    public void postRequestPatientAddConfirmationValidateShouldRedirectToPatientList() throws Exception {
        //1. Setup
        List<NoteModel> patientList = new ArrayList<>();
        patientList.add(patientModel1());

        //2. Act
        doNothing()
                .when(patientService)
                .savePatient(patientModel1());

        mockMvc.perform(post("/patient/add/confirmation/validate")
                .flashAttr("successSaveMessage", "Patient was successfully added")
                .param("id", "1")
                .param("givenName", "John")
                .param("familyName", "Boyd")
                .param("birthdate", "2020-01-01")
                .param("gender", "MALE")
                .param("address.street", "StreetTest1")
                .param("address.city", "CityTest1")
                .param("address.postcode", "112345")
                .param("address.district", "DistrictTest1")
                .param("address.state", "StateTest1")
                .param("address.country","CountryTest1")
                .param("emailAddress", "EmailTest1@email.com")
                .param("phoneNumber", "004678925899"))

                //3. Assert
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/patient/list"))
                .andExpect(flash().attributeExists("successSaveMessage"))
                .andReturn();
    }

    @Test
    public void getRequestPatientUpdateWithExistentIdShouldRedirectPatientUpdateView() throws Exception {
        //1. Setup
        List<NoteModel> patientList = new ArrayList<>();
        patientList.add(patientModel1());

        //2. Act
        doReturn(true)
                .when(patientService)
                .checkIdExists(1);

        doReturn(patientModel1())
                .when(patientService)
                .getPatientById(1);

        mockMvc.perform(get("/patient/update/{id}", "1"))

        //3. Assert
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("patient/update"))
                .andExpect(model().attributeExists("patient"))
                .andReturn();

        assertTrue(patientModel1().getFamilyName().equals("Boyd"));
    }

    @Test
    public void getRequestPatientUpdateWithNonExistentIdShouldRedirectToPatientListWithErrorPatientIdMessage() throws Exception {
        //1. Setup
        List<NoteModel> patientList = new ArrayList<>();
        patientList.add(patientModel1());

        //2. Act
        doReturn(false)
                .when(patientService)
                .checkIdExists(1);

        mockMvc.perform(get("/patient/update/{id}", "1")
                .flashAttr("ErrorPatientIdMessage", "Patient ID doesn't exist"))
        //3. Assert
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/patient/list"))
                .andExpect(flash().attributeExists("ErrorPatientIdMessage"))
                .andReturn();
    }

    @Test(expected = Exception.class)
    public void getRequestPatientUpdateWithInvalidIdShouldRedirectToPatientListWithErrorPatientIdMessage() throws Exception {
        //1. Setup
        List<NoteModel> patientList = new ArrayList<>();
        patientList.add(patientModel1());

        //2. Act
    //I don't know if these 3 next lines should be deleted or not
        doThrow(Exception.class)
                .when(patientService)
                .checkIdExists(1);

        mockMvc.perform(get("/patient/update/{id}", "5")
                .flashAttr("InvalidPatientIdMessage", "Invalid patient ID"))
                //3. Assert
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/patient/list"))
                .andExpect(flash().attributeExists("InvalidPatientIdMessage"))
                .andReturn();
    }

    @Test
    public void postRequestPatientPostUpdateWithNonExistentIdShouldRedirectToPatientList() throws Exception {
        //1. Setup
        //2. Act
        doReturn(false)
                .when(patientService)
                .checkIdExists(1);

        mockMvc.perform(post("/patient/update/{id}", "18")
                .flashAttr("ErrorPatientIdMessage", "Patient ID doesn't exist")
                .param("id", "1")
                .param("givenName", "John")
                .param("familyName", "Boyd")
                .param("birthdate", "2020-01-01")
                .param("gender", "MALE")
                .param("address.street", "StreetTest1")
                .param("address.city", "CityTest1")
                .param("address.postcode", "112345")
                .param("address.district", "DistrictTest1")
                .param("address.state", "StateTest1")
                .param("address.country","CountryTest1")
                .param("emailAddress", "EmailTest1@email.com")
                .param("phoneNumber", "004678925899"))
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
        doReturn(true)
                .when(patientService)
                .checkIdExists(1);
        doNothing()
                .when(patientService)
                .updatePatient(patientModel1());

        mockMvc.perform(post("/patient/update/{id}", "1")
                .flashAttr("successUpdateMessage", "Your patient was successfully updated")
                .param("id", "1")
                .param("givenName", "John")
                .param("familyName", "Boyd")
                .param("birthdate", "2020-01-01")
                .param("gender", "MALE")
                .param("address.street", "StreetTest1")
                .param("address.city", "CityTest1")
                .param("address.postcode", "112345")
                .param("address.district", "DistrictTest1")
                .param("address.state", "StateTest1")
                .param("address.country","CountryTest1")
                .param("emailAddress", "EmailTest1@email.com")
                .param("phoneNumber", "004678925899"))
        //3. Assert
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/patient/list"))
                .andExpect(flash().attributeExists("successUpdateMessage"))
                .andReturn();
    }

    @Test(expected = Exception.class)
    public void postRequestPatientPostUpdateWithInvalidIdShouldReturnPatientAddView() throws Exception {
        //1. Setup
        //2. Act
        doReturn(true)
                .when(patientService)
                .checkIdExists(1);

        doThrow(Exception.class)
                .when(patientService)
                .updatePatient(patientModel1());

        mockMvc.perform(post("/patient/update/{id}", "#{|#@#{[[["))
        //3. Assert
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("redirect:/patient/list"))
                .andReturn();
    }

    @Test
    public void getRequestPatientDeleteIdShouldReturnSuccess() throws Exception {
        //1. Setup
        List<NoteModel> patientList = new ArrayList<>();
        patientList.add(patientModel1());

        doReturn(true)
                .when(patientService)
                .checkIdExists(1);

        doNothing()
                .when(patientService)
                .deletePatientById(1);

        doReturn(patientList)
                .when(patientService)
                .getAllPatients();
        //2. Act
        mockMvc.perform(get("/patient/delete/{id}", "1")
                .flashAttr("successDeleteMessage", "This patient was successfully deleted"))
        //3. Assert
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/patient/list"))
                .andExpect(flash().attributeExists("successDeleteMessage"))
                .andReturn();
        assertTrue(patientModel1().getFamilyName().equals("Boyd"));
    }

    @Test
    public void getRequestPatientDeleteWithNonExistentIdShouldRedirectToPatientList() throws Exception {
        //1. Setup
        List<NoteModel> patientList = new ArrayList<>();
        patientList.add(patientModel1());

        doReturn(false)
                .when(patientService)
                .checkIdExists(1);
        //2. Act
        mockMvc.perform(get("/patient/delete/{id}", "25")
                .flashAttr("ErrorPatientIdMessage", "Patient ID doesn't exist"))
                //3. Assert
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/patient/list"))
                .andExpect(flash().attributeExists("ErrorPatientIdMessage"))
                .andReturn();
    }

    @Test(expected = Exception.class)
    public void getRequestPatientDeleteWithWrongIdShouldRedirectToPatientList() throws Exception {
        //1. Setup
        //2. Act
        doThrow(Exception.class)
                .when(patientService)
                .checkIdExists(1);

        mockMvc.perform(get("/patient/delete/{id}", "#{@[@[")
                .flashAttr("errorDeleteMessage", "Error during deletion of the patient"))
        //3. Assert
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/patient/list"))
                .andExpect(flash().attributeExists("errorDeleteMessage"))
                .andReturn();
    }
}

 */