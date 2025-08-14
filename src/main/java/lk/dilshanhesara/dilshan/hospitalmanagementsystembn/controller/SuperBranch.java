package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.controller;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.BranchDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.BranchSummaryDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Branch;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.BranchService;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/super/branch")
@RequiredArgsConstructor
public class SuperBranch {


    private final BranchService branchService;
    private final DashboardService dashboardService;

    @GetMapping("/summary")
    @PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<List<BranchSummaryDto>> getBranchSummaries() {
        return ResponseEntity.ok(branchService.getAllBranchSummaries());
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<List<BranchDto>> getAllBranches() {
        return ResponseEntity.ok(branchService.getAllBranches());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<Branch> addBranch(@RequestBody BranchDto branchDto) {
        Branch newBranch = branchService.addBranch(branchDto);
        return ResponseEntity.ok(newBranch);
    }



    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<Void> updateBranch(@PathVariable Long id, @RequestBody BranchDto branchDto) {
        branchService.updateBranch(id, branchDto);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<Void> deleteBranch(@PathVariable Long id) {
        branchService.deleteBranch(id);
        return ResponseEntity.ok().build();
    }
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<Void> updateBranchStatus(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        branchService.updateBranchStatus(id, payload.get("status"));
        return ResponseEntity.ok().build();
    }
}
