package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.PatientDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.StaffProfileDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PatientService {
    void addPatient(PatientDto patientDto);


    public List<PatientDto> getAllPatients() ;
    Patient getOrCreatePatientForOnlineUser(Integer onlineUserId);

    public Page<PatientDto> findPatientsByBranch(Long branchId, Pageable pageable) ;


    }

