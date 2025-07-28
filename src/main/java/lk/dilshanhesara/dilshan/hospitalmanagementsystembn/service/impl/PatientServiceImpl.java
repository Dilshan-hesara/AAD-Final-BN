package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.impl;



import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.PatientDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Patient;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.PatientRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;

    @Override
    public void addPatient(PatientDto patientDto) {
        // Convert the DTO to an Entity
        Patient patient = modelMapper.map(patientDto, Patient.class);

        // Save the new patient record to the database
        patientRepository.save(patient);
    }
}