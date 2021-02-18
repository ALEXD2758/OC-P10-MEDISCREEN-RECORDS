/*package com.mediscreen.records.services;

import com.mediscreen.records.model.AddressModel;
import com.mediscreen.records.model.NoteModel;
import com.mediscreen.records.repository.AddressRepository;
import com.mediscreen.records.repository.GenderEnum;
import com.mediscreen.records.repository.PatientRepository;
import org.joda.time.LocalDate;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

//import org.junit.jupiter.api.AfterAll;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AddressUTests {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private AddressRepository addressRepository;

    public NoteModel patientModel4() {
        AddressModel addressModel4 = new AddressModel();
        addressModel4.setId(1);
        addressModel4.setStreet("StreetTest1");
        addressModel4.setCity("CityTest1");
        addressModel4.setPostcode("112345");
        addressModel4.setDistrict("DistrictTest1");
        addressModel4.setState("StateTest1");
        addressModel4.setCountry("CountryTest1");

        LocalDate date = new LocalDate(2020, 01, 01);
        NoteModel patientModel4 = new NoteModel();
        patientModel4.setId(1);
        patientModel4.setGivenName("John");
        patientModel4.setFamilyName("Boyd");
        patientModel4.setBirthdate(date);
        patientModel4.setGender(GenderEnum.MALE);
        patientModel4.setAddress(addressModel4);
        patientModel4.setEmailAddress("EmailTest1@email.com");
        patientModel4.setPhoneNumber("004678925899");
        return patientModel4;
    }

    public NoteModel patientModel5() {
        AddressModel addressModel5 = new AddressModel();
        addressModel5.setId(2);
        addressModel5.setStreet("StreetTest2");
        addressModel5.setCity("CityTest2");
        addressModel5.setPostcode("212345");
        addressModel5.setDistrict("DistrictTest2");
        addressModel5.setState("StateTest2");
        addressModel5.setCountry("CountryTest2");

        LocalDate date = new LocalDate(2019, 01, 01);
        NoteModel patientModel5 = new NoteModel();
        patientModel5.setId(2);
        patientModel5.setGivenName("Roger");
        patientModel5.setFamilyName("Patterson");
        patientModel5.setBirthdate(date);
        patientModel5.setGender(GenderEnum.MALE);
        patientModel5.setAddress(addressModel5);
        patientModel5.setEmailAddress("EmailTest2@email.com");
        patientModel5.setPhoneNumber("004678925899");
        return patientModel5;
    }

    public NoteModel patientModel6() {
        AddressModel addressModel6 = new AddressModel();
        addressModel6.setId(3);
        addressModel6.setStreet("StreetTest2");
        addressModel6.setCity("CityTest2");
        addressModel6.setPostcode("212345");
        addressModel6.setDistrict("DistrictTest2");
        addressModel6.setState("StateTest2");
        addressModel6.setCountry("CountryTest2");

        LocalDate date = new LocalDate(2018, 01, 01);
        NoteModel patientModel6 = new NoteModel();
        patientModel6.setId(3);
        patientModel6.setGivenName("David");
        patientModel6.setFamilyName("Patterson");
        patientModel6.setBirthdate(date);
        patientModel6.setGender(GenderEnum.MALE);
        patientModel6.setAddress(addressModel6);
        patientModel6.setEmailAddress("EmailTest3@email.com");
        patientModel6.setPhoneNumber("0045899");
        return patientModel6;
    }

    @Before
    public void savePatientsToDbBeforeTests() throws SQLException {
        ScriptUtils.executeSqlScript(dataSource.getConnection(), new FileSystemResource("src/test/resources/db_test_scriptV2.sql"));
        patientRepository.deleteAll();
        patientRepository.save(patientModel4());
        patientRepository.save(patientModel5());
        patientRepository.save(patientModel6());
    }

    @After
    public void deleteAllAddressesAfterTests() {
        patientRepository.deleteAll();
    }

    @Test
    public void getAddressByIdShouldReturnAddressWithThatId() {
        //ARRANGE
        int id = 1;

        //ACT
        AddressModel address = addressRepository.findById(id);

        //ASSERT
        Assert.assertTrue(address.getStreet().equals("StreetTest1"));
        Assert.assertTrue(address.getPostcode().equals("112345"));
        Assert.assertTrue(address.getCity().equals("CityTest1"));
    }

    @Test
    public void getAllPatientsWithExistentAddressShouldReturnPatientsAtThatAddress() {
        //ARRANGE
        String street = "StreetTest2";
        String city = "CityTest2";
        String postcode = "212345";
        String country = "CountryTest2";

        //ACT
        List<AddressModel> listAddresses =
                addressRepository.findAllByStreetAndCityAndPostcodeAndCountry(street, city, postcode, country);

        //ASSERT
        Assert.assertTrue(listAddresses.size() == 2);
        Assert.assertTrue(listAddresses.get(0).getStreet().equals("StreetTest2"));
        Assert.assertTrue(listAddresses.get(1).getPostcode().equals("212345"));
        Assert.assertTrue(listAddresses.get(1).getCity().equals("CityTest2"));

    }
}

 */