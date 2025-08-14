package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.BranchDetailDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.BranchDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.BranchSummaryDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Branch;

import java.util.List;
import java.util.Map;

public interface BranchService {
    Branch getBranchById(Long branchId);

    Long getCurrentAdminBranchId();



    // --- ADD THIS NEW METHOD ---
    List<BranchDto> getAllBranches();

    public List<BranchSummaryDto> getAllBranchSummaries() ;
    public Branch addBranch(BranchDto branchDto);


    public void updateBranch(Long branchId, BranchDto branchDto) ;
    public void deleteBranch(Long branchId) ;
    public void updateBranchStatus(Long branchId, String status);
    }