/*package com.mediscreen.records.services;

import com.mediscreen.records.model.AddressModel;
import com.mediscreen.records.model.NoteModel;
import com.mediscreen.records.repository.AddressRepository;
import com.mediscreen.records.repository.GenderEnum;
import com.mediscreen.records.repository.PatientRepository;
import org.joda.time.LocalDate;
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
public class PatientUTests {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AddressRepository addressRepository;

    public NoteModel patientModel1() {
        AddressModel addressModel1 = new AddressModel();
        addressModel1.setStreet("StreetTest1");
        addressModel1.setCity("CityTest1");
        addressModel1.setPostcode("112345");
        addressModel1.setDistrict("DistrictTest1");
        addressModel1.setState("StateTest1");
        addressModel1.setCountry("CountryTest1");

        LocalDate date = new LocalDate(2020, 01, 01);
        NoteModel patientModel1 = new NoteModel();
        patientModel1.setGivenName("John");
        patientModel1.setFamilyName("Boyd");
        patientModel1.setBirthdate(date);
        patientModel1.setGender(GenderEnum.MALE);
        patientModel1.setAddress(addressModel1);
        patientModel1.setEmailAddress("EmailTest1@email.com");
        patientModel1.setPhoneNumber("004678925899");
        return patientModel1;
    }

    public NoteModel patientModel2() {
        AddressModel addressModel2 = new AddressModel();
        addressModel2.setStreet("StreetTest2");
        addressModel2.setCity("CityTest2");
        addressModel2.setPostcode("212345");
        addressModel2.setDistrict("DistrictTest2");
        addressModel2.setState("StateTest2");
        addressModel2.setCountry("CountryTest2");

        LocalDate date = new LocalDate(2014, 01,01);
        NoteModel patientModel2 = new NoteModel();
        patientModel2.setGivenName("Roger");
        patientModel2.setFamilyName("Patterson");
        patientModel2.setBirthdate(date);
        patientModel2.setGender(GenderEnum.MALE);
        patientModel2.setAddress(addressModel2);
        patientModel2.setEmailAddress("EmailTest2@email.com");
        patientModel2.setPhoneNumber("004678925899");
        return patientModel2;
    }

    @Before
    public void savePatientsToDbBeforeTests() throws SQLException {
        ScriptUtils.executeSqlScript(dataSource.getConnection(), new FileSystemResource("src/test/resources/db_test_scriptV2.sql"));
        patientRepository.deleteAll();
        patientRepository.save(patientModel1());
        patientRepository.save(patientModel2());
    }

    @After
    public void deleteAllPatientsAfterTests() {
        patientRepository.deleteAll();
    }

    @Test
    public void getAllPatientsShouldReturnAllPatients() {
        //ARRANGE
        //ACT
        List<NoteModel> listPatient = patientRepository.findAll();
        //ASSERT
        Assert.assertTrue(listPatient.size() == 2);
        Assert.assertTrue(listPatient.get(0).getGivenName().equals("John"));
        Assert.assertTrue(listPatient.get(0).getAddress().getStreet().equals("StreetTest1"));
    }

    @Test
    public void getAllPatientsByAddressShouldReturnPatientsAtThatAddress() {
        //ARRANGE
        String street = "StreetTest2";
        String city = "CityTest2";
        String country = "CountryTest2";

        //ACT
        List<NoteModel> listPatient =
                patientRepository.findAllByAddressStreetAndAddressCityAndAddressCountry(street, city, country);

        //ASSERT
        Assert.assertTrue(listPatient.size() == 1);
        Assert.assertTrue(listPatient.get(0).getGivenName().equals("Roger"));
        Assert.assertTrue(listPatient.get(0).getAddress().getStreet().equals("StreetTest2"));
    }

    @Test
    public void getPatientByIdShouldReturnPatientWithThatId() {
        //ARRANGE
        int id = 1;

        //ACT
        NoteModel patient = patientRepository.findById(id);

        //ASSERT
        Assert.assertTrue(patient.getGivenName().equals("John"));
        Assert.assertTrue(patient.getGender().equals(GenderEnum.MALE));
        Assert.assertTrue(patient.getAddress().getStreet().equals("StreetTest1"));
    }

    @Test
    public void checkExistentPatientByGivenFamilyNamesBirthDateShouldReturnTrue() {
        //ARRANGE
        String givenName = "Roger";
        String familyName = "Patterson";
        LocalDate birthdate = new LocalDate(2014, 01,01);

        //ACT
        boolean existentPatient = patientRepository.existsByGivenNameAndFamilyNameAndBirthdate(givenName, familyName,
                birthdate);

        //ASSERT
        Assert.assertEquals(true, existentPatient);
    }

    @Test
    public void checkExistentPatientByIdShouldReturnTrue() {
        //ARRANGE
        int id = 1;

        //ACT
        boolean existentPatientById = patientRepository.existsById(id);

        //ASSERT
        Assert.assertEquals(true, existentPatientById);
    }

    @Test
    public void deletePatientByIdShouldDeletePatient() {
        //ARRANGE
        int id = 1;

        //ACT
        boolean existentPatientById = patientRepository.existsById(id);
        patientRepository.deleteById(id);
        Optional<NoteModel> optionalPatientModel = Optional.ofNullable(patientRepository.findById(id));

        //ASSERT
        Assert.assertFalse(optionalPatientModel.isPresent());
    }

    @Test
    public void savePatientShouldSaveANewPatient() {
        //ARRANGE
        AddressModel addressModel3 = new AddressModel();
        addressModel3.setId(3);
        addressModel3.setStreet("StreetTest3");
        addressModel3.setCity("CityTest3");
        addressModel3.setPostcode("2123445");
        addressModel3.setDistrict("DistrictTest3");
        addressModel3.setState("StateTest3");
        addressModel3.setCountry("CountryTest3");

        LocalDate date = new LocalDate(2018,01,01);
        NoteModel patientModel3 = new NoteModel();
        patientModel3.setGivenName("Test");
        patientModel3.setFamilyName("TestName");
        patientModel3.setBirthdate(date);
        patientModel3.setGender(GenderEnum.FEMALE);
        patientModel3.setEmailAddress("EmailTest3@email.com");
        patientModel3.setPhoneNumber("004678925899");
        patientModel3.setAddress(addressModel3);


        AddressModel address = new AddressModel();
        address.setStreet(patientModel3.getAddress().getStreet());
        address.setCity(patientModel3.getAddress().getCity());
        address.setPostcode(patientModel3.getAddress().getPostcode());
        address.setDistrict(patientModel3.getAddress().getDistrict());
        address.setState(patientModel3.getAddress().getState());
        address.setCountry(patientModel3.getAddress().getCountry());
        patientModel3.setAddress(address);

        //ACT
        NoteModel patientToSave = patientRepository.saveAndFlush(patientModel3);
        patientRepository.flush();

        //ASSERT
        Assert.assertNotNull(patientToSave.getId());
        Assert.assertNotNull(patientToSave.getAddress().getStreet());
        Assert.assertEquals("Test", patientToSave.getGivenName());
        Assert.assertEquals("StreetTest3", patientToSave.getAddress().getStreet());
    }

    @Test
    public void updatePatientShouldUpdatePatient() {
        //ARRANGE
        AddressModel addressToUpdate = addressRepository.findById(2);
        addressToUpdate.setStreet("Street4");
        addressToUpdate.setCity("CityTest4");
        addressToUpdate.setPostcode("1234");
        addressToUpdate.setDistrict("District4");
        addressToUpdate.setState("State4");
        addressToUpdate.setCountry("Country4");

        //ACT
        NoteModel patientModel3 = patientRepository.findById(2);
        patientModel3.setAddress(addressToUpdate);
        patientModel3.setFamilyName("UpdatedName");
        NoteModel patientToUpdate = patientRepository.save(patientModel3);

        //ASSERT
        Assert.assertEquals("CityTest4", patientToUpdate.getAddress().getCity());
        Assert.assertEquals("UpdatedName", patientToUpdate.getFamilyName());
        Assert.assertEquals("1234", patientToUpdate.getAddress().getPostcode());
    }
}
 */