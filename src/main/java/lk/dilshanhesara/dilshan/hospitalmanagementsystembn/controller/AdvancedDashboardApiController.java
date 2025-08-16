package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.controller;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.AdvancedBranchDashboardDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Appointment;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.AdvancedDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/branch-dashboard")
@RequiredArgsConstructor
public class AdvancedDashboardApiController {
    private final AdvancedDashboardService advancedDashboardService;
    @GetMapping("/{branchId}")
    public ResponseEntity<AdvancedBranchDashboardDto> getAdvancedSummary(
            @PathVariable Long branchId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        return ResponseEntity.ok(advancedDashboardService.getAdvancedDashboardSummary(branchId, startDate, endDate));
    }
}
