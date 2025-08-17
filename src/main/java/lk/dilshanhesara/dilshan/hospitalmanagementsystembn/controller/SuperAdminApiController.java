package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.controller;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.PatientDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.SuperAdminDashboardDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.PatientService;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.SuperAdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/super-admin")
@RequiredArgsConstructor
public class SuperAdminApiController {

    private final SuperAdminDashboardService dashboardService;
    private final PatientService patientService;

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

}