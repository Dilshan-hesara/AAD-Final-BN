package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.controller;


import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.BranchDashboardDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.BranchDetailDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.BranchDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Branch;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.BranchService;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import java.util.Map;

@RestController
@RequestMapping("/api/branch")
@RequiredArgsConstructor
public class BranchApiController {

    private final BranchService branchService;
    private final DashboardService dashboardService;


    // In BranchApiController.java
//    @GetMapping("/{id}/dashboard-details")
//    public BranchDashboardDto getBranchDashboardDetails(@PathVariable Long id) {
//        Branch branch = branchService.getBranchById(id);
//        Map<String, Long> stats = dashboardService.getBranchStatistics(id);
//
//        return new BranchDashboardDto(
//                branch.getName(),
//                branch.getLocation(),
//                branch.getContactNumber(),
//                stats.get("receptionistCount"), // NEW
//                stats.get("doctorCount"),
//                stats.get("appointmentsToday"),
//                stats.get("confirmedCount"),
//                stats.get("completedCount"),
//                stats.get("cancelledCount"),
//                stats.get("totalAppointmentsToday")
//        );
//    }



    @GetMapping("/{id}/dashboard-details")
    public BranchDashboardDto getBranchDashboardDetails(@PathVariable Long id) {
        Branch branch = branchService.getBranchById(id);
        Map<String, Object> stats = dashboardService.getBranchStatistics(id);

        return new BranchDashboardDto(
                branch.getName(),
                branch.getLocation(),
                branch.getContactNumber(),
                (Long) stats.get("receptionistCount"),
                (Long) stats.get("doctorCount"),
                (Long) stats.get("confirmedCount"),
                (Long) stats.get("completedCount"),
                (Long) stats.get("cancelledCount"),
                (Long) stats.get("onlineAppointments"),
                (Long) stats.get("walkInAppointments"),
                (Double) stats.get("todaysEarnings")
        );
    }
    @GetMapping
    public ResponseEntity<List<BranchDto>> getAllBranches() {
        // You'll need a method in your BranchService to get all branches as DTOs
        return ResponseEntity.ok(branchService.getAllBranches());
    }


    // In BranchApiController.java

}