package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.impl;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Branch;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.StaffProfile;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.UserAccount;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.BranchRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.StaffProfileRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.UserAccountRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class BranchServiceImpl implements BranchService {

    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private StaffProfileRepository staffProfileRepository;
    @Autowired
    private UserAccountRepository userAccountRepository;

    @Override
    public Branch getBranchById(Long branchId) {
        return branchRepository.findById(branchId)
                .orElseThrow(() -> new RuntimeException("Branch not found with id: " + branchId));
    }

    @Override
    public Long getCurrentAdminBranchId() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();


        UserAccount account = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User account not found for username: " + username));


        StaffProfile profile = staffProfileRepository.findById(account.getUserId())
                .orElseThrow(() -> new RuntimeException("Staff profile not found for user ID: " + account.getUserId()));

        if (profile.getBranch() == null) {
            throw new RuntimeException("Admin is not assigned to any branch.");
        }

        return profile.getBranch().getId();
    }
}