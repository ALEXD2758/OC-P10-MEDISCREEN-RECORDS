package com.mediscreen.records.service;

import com.mediscreen.records.model.PatientModel;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class PatientWebClientService {

    // Declare the base url (to use with Docker)
    private final String BASE_URL = "http://patients:8081";
    // Declare the path for patient list (to use with localhost)
    private final String BASE_URL_LOCALHOST = "http://localhost:8081";
    // Declare the path for patient list
    private final String PATH_PATIENT_LIST = "/getPatientList";
    // Declare the path for checking a patient ID
    private final String PATH_PATIENT_EXIST = "/checkPatientId";
    //Declare the AttractionId name to use in the request of the Rest Template Web Client
    private final String USER_ID = "?patientId=";

    //Define the patients service URI (for patient list)
    private final String getListPatientServiceUri() {
        return BASE_URL_LOCALHOST + PATH_PATIENT_LIST;
    }

    //Define the patients service URI (for checkPatientIdExist)
    private final String getCheckPatientIdServiceUri() {
        return BASE_URL_LOCALHOST + PATH_PATIENT_EXIST + USER_ID;
    }

    /**
     * Web Client request to server-service "patients" for getting a list of all patients
     *
     * @return PatientModel list of all patients
     */
    public List<PatientModel> getListPatients() {
        Flux<PatientModel> getPatientList= WebClient.create()
                .get()
                .uri(getListPatientServiceUri())
                .retrieve()
                .bodyToFlux(PatientModel.class);
        List<PatientModel> patientList = getPatientList.collectList().block();
        return patientList;
    }

    /**
     * Web Client request to server-service "patients" to check if a patient Id exists
     *
     * @param patientId int
     * @return boolean of patient id exists' query
     */
    public boolean checkPatientIdExist(int patientId) {
        Mono<Boolean> getPatientList= WebClient.create()
                .get()
                .uri(getCheckPatientIdServiceUri() + patientId)
                .retrieve()
                .bodyToMono(Boolean.class);
        boolean patientList = getPatientList.block();
        return patientList;
    }
}