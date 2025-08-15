package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.controller;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.SuperAdminDashboardDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.SuperAdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/super-admin")
@RequiredArgsConstructor
public class SuperAdminApiController {

    private final SuperAdminDashboardService dashboardService;

    @GetMapping("/dashboard-summary")
    public ResponseEntity<SuperAdminDashboardDto> getSummary() {
        return ResponseEntity.ok(dashboardService.getDashboardSummary());
    }
}