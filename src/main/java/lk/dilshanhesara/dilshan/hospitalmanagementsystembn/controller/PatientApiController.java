package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.controller;


import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.PatientDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.StaffProfileDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.PatientService;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.StaffProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientApiController {

    private final PatientService patientService;



    private final StaffProfileService staffProfileService;



    // In Patient load  karananva book   appoiment combobox eek ta adalai
    @GetMapping("/search")
    public ResponseEntity<Page<PatientDto>> searchPatients(
            @RequestParam(defaultValue = "") String name,
            Pageable pageable) {
        return ResponseEntity.ok(patientService.searchPatientsByName(name, pageable));
    }
    @GetMapping("/branch")
    public ResponseEntity<Page<PatientDto>> getPatientsForCurrentBranch(Pageable pageable) {
        StaffProfileDto adminProfile = staffProfileService.getCurrentLoggedInStaffProfile();
        Page<PatientDto> patients = patientService.findPatientsByBranch(adminProfile.getBranchId(), pageable);
        return ResponseEntity.ok(patients);
    }

////    metanai yata tika pagtion page ekta adali
    @GetMapping
    public ResponseEntity<Page<PatientDto>> searchPatientsPage(
            @RequestParam(required = false) String keyword,
            Pageable pageable) {
        Page<PatientDto> patients = patientService.searchPatientsPage(keyword, pageable);
        return ResponseEntity.ok(patients);
    }

    @PostMapping
    public ResponseEntity<Void> addPatient(@RequestBody PatientDto patientDto) {
        patientService.addPatient(patientDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePatient(@PathVariable Long id, @RequestBody PatientDto patientDto) {
        patientService.updatePatientPage(id, patientDto);
        return ResponseEntity.ok().build();
    }



}