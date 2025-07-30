package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.PatientDto;

import java.util.List;

public interface PatientService {
    void addPatient(PatientDto patientDto);


    public List<PatientDto> getAllPatients() ;

    }

