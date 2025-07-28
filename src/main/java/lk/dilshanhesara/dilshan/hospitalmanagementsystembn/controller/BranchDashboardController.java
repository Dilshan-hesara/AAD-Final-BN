package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.controller;


import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Branch;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.BranchService;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
@RequestMapping("/branch")
public class BranchDashboardController {

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private BranchService branchService; // Service to get branch details

    @GetMapping("/dashboard")
    public String showBranchDashboard(Model model) {

        Long branchId = branchService.getCurrentAdminBranchId();

        Branch currentBranch = branchService.getBranchById(branchId);

        Map<String, Long> stats = dashboardService.getBranchStatistics(branchId);

        model.addAttribute("currentBranch", currentBranch);
        model.addAttribute("stats", stats);

        return "branch-dashboard";
    }
}