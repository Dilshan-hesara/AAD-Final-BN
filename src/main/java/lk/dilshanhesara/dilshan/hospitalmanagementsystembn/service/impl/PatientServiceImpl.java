package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.impl;



import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.transaction.Transactional;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.PatientDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Appointment;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.OnlineUserProfile;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Patient;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.AppointmentRepository;
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
    private final AppointmentRepository appointmentRepository;


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
        Specification<Patient> spec = (root, query, cb) -> {
            if (keyword == null || keyword.isEmpty()) {
                return cb.conjunction();
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


    @Override
    public Page<PatientDto> searchAllPatients(String keyword, Long branchId, Pageable pageable) {

        Specification<Patient> spec = (root, query, cb) -> {
            query.distinct(true);
            return null;
        };

        if (keyword != null && !keyword.isEmpty()) {
            spec = spec.and((root, query, cb) ->
                    cb.or(
                            cb.like(root.get("fullName"), "%" + keyword + "%"),
                            cb.like(root.get("contactNumber"), "%" + keyword + "%")
                    )
            );
        }

        if (branchId != null) {
            spec = spec.and((root, query, cb) -> {
                Join<Patient, Appointment> appointmentJoin = root.join("appointments", JoinType.INNER);
                return cb.equal(appointmentJoin.get("branch").get("id"), branchId);
            });
        }

        Page<Patient> patients = patientRepository.findAll(spec, pageable);

        return patients.map(patient -> {
            PatientDto dto = modelMapper.map(patient, PatientDto.class);

            appointmentRepository.findTopByPatientIdOrderByAppointmentDateDesc(patient.getId())
                    .ifPresent(latestAppointment -> {
                        dto.setBranchName(latestAppointment.getBranch().getName());
                    });

            return dto;
        });
    }




    @Override
    public PatientDto findPatientById(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));

        PatientDto dto = modelMapper.map(patient, PatientDto.class);

        appointmentRepository.findTopByPatientIdOrderByAppointmentDateDesc(patient.getId())
                .ifPresent(latestAppointment -> {
                    if (latestAppointment.getBranch() != null) {
                        dto.setBranchName(latestAppointment.getBranch().getName());
                    }
                });

        return dto;

    }



    @Override
    @Transactional
    public Patient getOrCreatePatientForOnlineUser(Integer onlineUserId) {
        return patientRepository.findByLinkedOnlineUser_UserId(onlineUserId)
                .orElseGet(() -> {
                    OnlineUserProfile profile = onlineUserProfileRepository.findById(onlineUserId)
                            .orElseThrow(() -> new RuntimeException("Online user profile not found"));

                    Patient newPatient = new Patient();
                    newPatient.setFullName(profile.getFullName());
                    newPatient.setContactNumber(profile.getContactNumber());
                    newPatient.setEmail(profile.getEmail());
                    newPatient.setLinkedOnlineUser(profile.getUserAccount());

                    return patientRepository.save(newPatient);
                });
    }
}