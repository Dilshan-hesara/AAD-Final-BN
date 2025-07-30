package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.controller;


import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.PatientDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientApiController {

    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<Void> addPatient(@RequestBody PatientDto patientDto) {
        patientService.addPatient(patientDto);
        return ResponseEntity.ok().build();
    }


    @GetMapping
    public ResponseEntity<List<PatientDto>> getAllPatients() {
        List<PatientDto> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }
}