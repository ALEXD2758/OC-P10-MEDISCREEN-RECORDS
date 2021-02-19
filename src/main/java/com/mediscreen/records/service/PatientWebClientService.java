package com.mediscreen.records.service;

import com.mediscreen.records.model.PatientModel;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class PatientWebClientService {

    // Declare the base url
    private final String BASE_URL = "http://localhost:8081";
    // Declare the path for patient list
    private final String PATH_PATIENT_LIST = "/getPatientList";
    // Declare the path for
    private final String PATH_PATIENT_EXIST = "/checkPatientId";

    //Define the patients service URI (for patient list)
    private final String getListPatientServiceUri() {
        return BASE_URL + PATH_PATIENT_LIST;
    }

    //Define the patients service URI
    private final String getCheckPatientIdServiceUri() {
        return BASE_URL + PATH_PATIENT_EXIST;
    }

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
     * Web Client request to server-service "patients"
     *
     * @param patientId int
     * @return boolean of patient id exists' query
     */
    public boolean checkPatientIdExist(int patientId) {
        Mono<Boolean> getPatientList= WebClient.create()
                .get()
                .uri(getCheckPatientIdServiceUri())
                .retrieve()
                .bodyToMono(Boolean.class);
        boolean patientList = getPatientList.block();
        return patientList;
    }
}