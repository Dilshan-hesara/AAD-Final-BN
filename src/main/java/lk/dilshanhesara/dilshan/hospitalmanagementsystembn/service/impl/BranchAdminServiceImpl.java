package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.impl;

import jakarta.persistence.criteria.Join;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.AdminUpdateRequestDto;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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


//    @Override
//    public Page<StaffProfileDto> getAllBranchAdmins(Pageable pageable) {
//        Page<UserAccount> accounts = userAccountRepository.findByRoleWithProfile(UserAccount.Role.BRANCH_ADMIN, pageable);
//        return accounts.map(this::convertToStaffProfileDto);
//    }
//    @Override
//    @Transactional
//    public void addBranchAdmin(StaffCreationRequestDto dto) {
//        Branch branch = branchRepository.findById(dto.getBranchId()).orElseThrow(() -> new RuntimeException("Branch not found"));
//
//        UserAccount account = new UserAccount();
//        account.setUsername(dto.getUsername());
//        account.setPassword(passwordEncoder.encode(dto.getPassword()));
//        account.setRole(UserAccount.Role.BRANCH_ADMIN);
//        account.setActive(true);
//        account = userAccountRepository.save(account);
//
//
//        StaffProfile profile = new StaffProfile();
//        profile.setUserAccount(account);
//        profile.setFullName(dto.getFullName());
//        profile.setBranch(branch);
//        profile.setEmail(dto.getEmail());
//        profile.setContactNumber(dto.getContactNumber());
//        staffProfileRepository.save(profile);
//    }
//
//    @Override
//    public Page<StaffProfileDto> getAllBranchAdmins(Pageable pageable) {
//        // Use the new repository method that supports sorting by name
//        Page<UserAccount> accounts = userAccountRepository.findBranchAdmins(pageable);
//        return accounts.map(this::convertToStaffProfileDto);
//    }
//
//    @Override
//    @Transactional
//    public void updateBranchAdmin(Integer userId, AdminUpdateRequestDto dto) {
//        UserAccount account = userAccountRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User account not found"));
//
//        StaffProfile profile = staffProfileRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("Staff profile not found"));
//
//        // Update details in StaffProfile
//        profile.setFullName(dto.getFullName());
//        profile.setEmail(dto.getEmail());
//        profile.setContactNumber(dto.getContactNumber());
//
//        // Update details in UserAccount
//        account.setUsername(dto.getUsername());
//
//        // Only update the password if a new one is provided
//        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
//            account.setPassword(passwordEncoder.encode(dto.getPassword()));
//        }
//
//        staffProfileRepository.save(profile);
//        userAccountRepository.save(account);
//    }
//
//
//    @Override
//    public void deleteBranchAdmin(Integer userId) {
//        userAccountRepository.deleteById(userId);
//    }
//    @Override
//    public void updateUserStatus(Integer userId, boolean isActive) {
//        UserAccount account = userAccountRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User account not found"));
//        account.setActive(isActive);
//        userAccountRepository.save(account);
//    }
//
//    private StaffProfileDto convertToStaffProfileDto(UserAccount account) {
//        StaffProfile profile = staffProfileRepository.findById(account.getUserId()).orElse(new StaffProfile());
//        StaffProfileDto dto = new StaffProfileDto();
//        dto.setUserId(account.getUserId());
//        dto.setUsername(account.getUsername());
//        dto.setFullName(profile.getFullName());
//        dto.setActive(account.isActive());
//        if (profile.getBranch() != null) {
//            dto.setBranchName(profile.getBranch().getName());
//        }
//        return dto;
//    }
//
//


    @Override
    public Page<StaffProfileDto> searchBranchAdmins(String keyword, Long branchId, Pageable pageable) {
        // This implementation is correct
        Specification<UserAccount> spec = (root, query, cb) -> {
            Join<UserAccount, StaffProfile> profileJoin = root.join("staffProfile");
            return cb.equal(root.get("role"), UserAccount.Role.BRANCH_ADMIN);
        };

        if (keyword != null && !keyword.isEmpty()) {
            spec = spec.and((root, query, cb) -> {
                Join<UserAccount, StaffProfile> profileJoin = root.join("staffProfile");
                return cb.like(profileJoin.get("fullName"), "%" + keyword + "%");
            });
        }

        if (branchId != null) {
            spec = spec.and((root, query, cb) -> {
                Join<UserAccount, StaffProfile> profileJoin = root.join("staffProfile");
                return cb.equal(profileJoin.get("branch").get("id"), branchId);
            });
        }

        Page<UserAccount> accounts = userAccountRepository.findAll(spec, pageable);
        return accounts.map(this::convertToStaffProfileDto);
    }

    @Override
    public StaffProfileDto findBranchAdminById(Integer userId) {
        UserAccount account = userAccountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Branch Admin not found"));
        return convertToStaffProfileDto(account);
    }

    @Override
    @Transactional
    public void addBranchAdmin(StaffCreationRequestDto dto) {
        if (userAccountRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        Branch branch = branchRepository.findById(dto.getBranchId())
                .orElseThrow(() -> new RuntimeException("Branch not found"));

        UserAccount account = new UserAccount();
        account.setUsername(dto.getUsername());
        account.setPassword(passwordEncoder.encode(dto.getPassword()));
        account.setRole(UserAccount.Role.BRANCH_ADMIN);
        account.setActive(true);
        account = userAccountRepository.save(account);

        StaffProfile profile = new StaffProfile();
        profile.setUserAccount(account);
        profile.setFullName(dto.getFullName());
        profile.setEmail(dto.getEmail());
        profile.setContactNumber(dto.getContactNumber());
        profile.setBranch(branch);
        staffProfileRepository.save(profile);
    }

    @Override
    @Transactional
    public void updateBranchAdmin(Integer userId, AdminUpdateRequestDto dto) {
        UserAccount account = userAccountRepository.findById(userId).orElseThrow();
        StaffProfile profile = staffProfileRepository.findById(userId).orElseThrow();

        account.setUsername(dto.getUsername());
        profile.setFullName(dto.getFullName());
        profile.setEmail(dto.getEmail());
        profile.setContactNumber(dto.getContactNumber());

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            account.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        userAccountRepository.save(account);
        staffProfileRepository.save(profile);
    }

    @Override
    public void deleteBranchAdmin(Integer userId, String currentUsername) {
        UserAccount accountToDelete = userAccountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (accountToDelete.getUsername().equals(currentUsername)) {
            throw new IllegalArgumentException("You cannot delete your own account.");
        }
        userAccountRepository.deleteById(userId);
    }

    @Override
    public void updateUserStatus(Integer userId, boolean isActive, String currentUsername) {
        UserAccount account = userAccountRepository.findById(userId).orElseThrow();
        if (account.getUsername().equals(currentUsername)) {
            throw new IllegalArgumentException("You cannot deactivate your own account.");
        }
        account.setActive(isActive);
        userAccountRepository.save(account);
    }

    private StaffProfileDto convertToStaffProfileDto(UserAccount account) {
        StaffProfile profile = staffProfileRepository.findById(account.getUserId())
                .orElse(new StaffProfile());
        StaffProfileDto dto = new StaffProfileDto();
        dto.setUserId(account.getUserId());
        dto.setUsername(account.getUsername());
        dto.setFullName(profile.getFullName());
        dto.setEmail(profile.getEmail());
        dto.setContactNumber(profile.getContactNumber());
        dto.setActive(account.isActive());
        if (profile.getBranch() != null) {
            dto.setBranchName(profile.getBranch().getName());
        }
        return dto;
    }
}