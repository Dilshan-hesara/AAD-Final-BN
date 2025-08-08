package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.PatientDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Patient;

import java.util.List;

public interface PatientService {
    void addPatient(PatientDto patientDto);


    public List<PatientDto> getAllPatients() ;
    Patient getOrCreatePatientForOnlineUser(Integer onlineUserId);

    }

