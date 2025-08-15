package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.impl;



import jakarta.transaction.Transactional;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.PatientDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.OnlineUserProfile;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Patient;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.OnlineUserProfileRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.PatientRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;


    @Override
    public void addPatient(PatientDto patientDto) {
        Patient patient = modelMapper.map(patientDto, Patient.class);

        patientRepository.save(patient);
    }



    @Override
    public List<PatientDto> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        return modelMapper.map(patients, new TypeToken<List<PatientDto>>() {}.getType());
    }


    private final OnlineUserProfileRepository onlineUserProfileRepository;

    @Override
    @Transactional
    public Patient getOrCreatePatientForOnlineUser(Integer onlineUserId) {
        // 1. Check if a patient record already exists for this online user
        return patientRepository.findByLinkedOnlineUser_UserId(onlineUserId)
                .orElseGet(() -> {
                    // 2. If not, find their profile
                    OnlineUserProfile profile = onlineUserProfileRepository.findById(onlineUserId)
                            .orElseThrow(() -> new RuntimeException("Online user profile not found"));

                    // 3. Create a new patient record using their profile info
                    Patient newPatient = new Patient();
                    newPatient.setFullName(profile.getFullName());
                    newPatient.setContactNumber(profile.getContactNumber());
                    newPatient.setEmail(profile.getEmail());
                    newPatient.setLinkedOnlineUser(profile.getUserAccount());

                    // 4. Save and return the new patient record
                    return patientRepository.save(newPatient);
                });
    }


    public Page<PatientDto> findPatientsByBranch(Long branchId, Pageable pageable) {
        Page<Patient> patients = patientRepository.findPatientsByBranch(branchId, pageable);
        return patients.map(patient -> modelMapper.map(patient, PatientDto.class));
    }
    public Page<PatientDto> searchPatientsByName(String name, Pageable pageable) {
        Page<Patient> patients = patientRepository.findByFullNameContainingIgnoreCase(name, pageable);
        return patients.map(patient -> modelMapper.map(patient, PatientDto.class));

    }

    @Override
    public Page<PatientDto> searchPatientsPage(String keyword, Pageable pageable) {
        // Create a specification for searching by full name or contact number
        Specification<Patient> spec = (root, query, cb) -> {
            if (keyword == null || keyword.isEmpty()) {
                return cb.conjunction(); // Return all if no keyword
            }
            String pattern = "%" + keyword.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("fullName")), pattern),
                    cb.like(root.get("contactNumber"), pattern)
            );
        };

        Page<Patient> patients = patientRepository.findAll(spec, pageable);
        return patients.map(patient -> modelMapper.map(patient, PatientDto.class));
    }

    @Override
    public void updatePatientPage(Long patientId, PatientDto patientDto) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        patient.setFullName(patientDto.getFullName());
        patient.setEmail(patientDto.getEmail());
        patient.setContactNumber(patientDto.getContactNumber());
        patient.setDateOfBirth(patientDto.getDateOfBirth());
        patient.setGender(patientDto.getGender());
        patient.setAddress(patientDto.getAddress());

        patientRepository.save(patient);
    }
}