package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.BranchDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Branch;

import java.util.List;

public interface BranchService {
    Branch getBranchById(Long branchId);

    Long getCurrentAdminBranchId();



    // --- ADD THIS NEW METHOD ---
    List<BranchDto> getAllBranches();
}