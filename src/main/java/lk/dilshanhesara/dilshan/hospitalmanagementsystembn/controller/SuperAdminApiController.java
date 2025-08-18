package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.controller;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.DoctorDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.PatientDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.SuperAdminDashboardDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.DoctorService;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.PatientService;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.SuperAdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/super-admin")
@RequiredArgsConstructor
public class SuperAdminApiController {

    private final SuperAdminDashboardService dashboardService;
    private final PatientService patientService;
    private final DoctorService doctorService;

    @GetMapping("/dashboard-summary")
    public ResponseEntity<SuperAdminDashboardDto> getSummary() {
        return ResponseEntity.ok(dashboardService.getDashboardSummary());
    }


    @GetMapping("/all-patients")
    public ResponseEntity<Page<PatientDto>> getAllPatients(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long branchId,
            Pageable pageable) {
        Page<PatientDto> patients = patientService.searchAllPatients(keyword, branchId, pageable);
        return ResponseEntity.ok(patients);
    }



    @GetMapping("/patients/{id}")
    public ResponseEntity<PatientDto> getPatientById(@PathVariable Long id) {
        PatientDto patient = patientService.findPatientById(id);
        return ResponseEntity.ok(patient);
    }


    // --- ADD THESE NEW ENDPOINTS ---
    @GetMapping("/all-doctors")
    public ResponseEntity<Page<DoctorDto>> getAllDoctors(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long branchId,
            Pageable pageable) {
        Page<DoctorDto> doctors = doctorService.searchAllDoctors(keyword, branchId, pageable);
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/doctors/{id}")
    public ResponseEntity<DoctorDto> getDoctorById(@PathVariable Integer id) {
        // You will need to create a findDoctorById service method
        DoctorDto doctor = doctorService.findDoctorById(id);
        return ResponseEntity.ok(doctor);
    }
}