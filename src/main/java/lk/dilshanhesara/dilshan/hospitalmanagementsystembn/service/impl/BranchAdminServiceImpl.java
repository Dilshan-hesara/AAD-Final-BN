package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.impl;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.StaffCreationRequestDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.StaffProfileDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Branch;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.StaffProfile;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.UserAccount;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.BranchRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.StaffProfileRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.UserAccountRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.BranchAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BranchAdminServiceImpl implements BranchAdminService {

    private final UserAccountRepository userAccountRepository;
    private final StaffProfileRepository staffProfileRepository;
    private final BranchRepository branchRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
        public Page<StaffProfileDto> getAllBranchAdmins(Pageable pageable) {
        Page<UserAccount> accounts = userAccountRepository.findByRole(UserAccount.Role.BRANCH_ADMIN, pageable);
        return accounts.map(this::convertToStaffProfileDto);
    }

    @Override
    @Transactional
    public void addBranchAdmin(StaffCreationRequestDto dto) {
        Branch branch = branchRepository.findById(dto.getBranchId()).orElseThrow(() -> new RuntimeException("Branch not found"));

        UserAccount account = new UserAccount();
        account.setUsername(dto.getUsername());
        account.setPassword(passwordEncoder.encode(dto.getPassword()));
        account.setRole(UserAccount.Role.BRANCH_ADMIN);
        account.setActive(true);
        account = userAccountRepository.save(account);

        StaffProfile profile = new StaffProfile();
        profile.setUserAccount(account);
        profile.setFullName(dto.getFullName());
        profile.setBranch(branch);
        staffProfileRepository.save(profile);
    }

    @Override
    public void updateBranchAdmin(Integer userId, StaffProfileDto dto) {
        StaffProfile profile = staffProfileRepository.findById(userId).orElseThrow(() -> new RuntimeException("Admin not found"));
        profile.setFullName(dto.getFullName());
        staffProfileRepository.save(profile);
    }

    @Override
    public void deleteBranchAdmin(Integer userId) {
        userAccountRepository.deleteById(userId);
    }

    private StaffProfileDto convertToStaffProfileDto(UserAccount account) {
        StaffProfile profile = staffProfileRepository.findById(account.getUserId()).orElse(new StaffProfile());
        StaffProfileDto dto = new StaffProfileDto();
        dto.setUserId(account.getUserId());
        dto.setUsername(account.getUsername());
        dto.setFullName(profile.getFullName());
        if (profile.getBranch() != null) {
            dto.setBranchName(profile.getBranch().getName());
        }
        return dto;
    }
}