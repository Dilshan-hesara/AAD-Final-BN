package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Branch;

public interface BranchService {
    Branch getBranchById(Long branchId);
    Long getCurrentAdminBranchId();
}