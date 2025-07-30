package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.impl;



import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.PatientDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Patient;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.PatientRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.util.List;

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



    @Override
    public List<PatientDto> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        // Convert the list of entities to a list of DTOs
        return modelMapper.map(patients, new TypeToken<List<PatientDto>>() {}.getType());
    }
}