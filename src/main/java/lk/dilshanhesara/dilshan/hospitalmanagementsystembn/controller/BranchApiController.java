package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.controller;


import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.BranchDashboardDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Branch;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.BranchService;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import java.util.Map;

@RestController
@RequestMapping("/api/branch")
@RequiredArgsConstructor
public class BranchApiController {

    private final BranchService branchService;
    private final DashboardService dashboardService;

    @GetMapping("/{id}/dashboard-details")
    public BranchDashboardDto getBranchDashboardDetails(@PathVariable Long id) {
        Branch branch = branchService.getBranchById(id);
        Map<String, Long> stats = dashboardService.getBranchStatistics(id);

        return new BranchDashboardDto(
                branch.getName(),
                branch.getLocation(),
                branch.getContactNumber(),
                stats.get("patientCount"),
                stats.get("doctorCount"),
                stats.get("appointmentsToday")
        );
    }
}