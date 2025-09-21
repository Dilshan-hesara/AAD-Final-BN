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
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.ReceptionistService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReceptionistServiceImpl implements ReceptionistService {

    private final UserAccountRepository userAccountRepository;
    private final StaffProfileRepository staffProfileRepository;
    private final BranchRepository branchRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public void addReceptionist(StaffCreationRequestDto dto) {
        Branch branch = branchRepository.findById(dto.getBranchId()).orElseThrow();

        UserAccount account = new UserAccount();
        account.setUsername(dto.getUsername());
        account.setPassword(passwordEncoder.encode(dto.getPassword()));
        account.setRole(UserAccount.Role.RECEPTIONIST);
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
    public List<StaffProfileDto> findReceptionistsByBranch(Long branchId) {
        return userAccountRepository.findReceptionistsByBranch(branchId).stream()
                .map(account -> {
                    StaffProfile profile = staffProfileRepository.findById(account.getUserId()).orElse(new StaffProfile());
                    StaffProfileDto dto = new StaffProfileDto();
                    dto.setUserId(account.getUserId());
                    dto.setUsername(account.getUsername());
                    dto.setRole(account.getRole().name());
                    dto.setFullName(profile.getFullName());

                    dto.setActive(account.isActive());

                    if (profile.getBranch() != null) {
                        dto.setBranchName(profile.getBranch().getName());
                    }
                    return dto;
                }).collect(Collectors.toList());
    }



    @Override
    public void updateUserStatus(Integer userId, boolean isActive) {
        UserAccount account = userAccountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User account not found"));

        account.setActive(isActive);
        userAccountRepository.save(account);
    }


    public Page<StaffProfileDto> searchReceptionists(Long branchId, String name, Pageable pageable) {
        Specification<UserAccount> spec = (root, query, cb) -> {
            var staffProfileJoin = root.join("staffProfile");
            return cb.and(
                    cb.equal(root.get("role"), UserAccount.Role.RECEPTIONIST),
                    cb.equal(staffProfileJoin.get("branch").get("id"), branchId)
            );
        };

        if (name != null && !name.isEmpty()) {
            spec = spec.and((root, query, cb) -> {
                var staffProfileJoin = root.join("staffProfile");
                return cb.like(staffProfileJoin.get("fullName"), "%" + name + "%");
            });
        }

        Page<UserAccount> accounts = userAccountRepository.findAll(spec, pageable);
        return accounts.map(this::convertToStaffProfileDto);
    }

    @Override
    public void updateReceptionist(Integer userId, StaffProfileDto dto) {
        StaffProfile profile = staffProfileRepository.findById(userId).orElseThrow();
        profile.setFullName(dto.getFullName());
        profile.setEmail(dto.getEmail());
        profile.setContactNumber(dto.getContactNumber());
        staffProfileRepository.save(profile);
    }






    @Override
    public Page<StaffProfileDto> searchAllReceptionists(String keyword, Long branchId, Pageable pageable) {

        Sort.Order sortOrder = pageable.getSort().getOrderFor("fullName");
        if (sortOrder != null) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                    Sort.by(sortOrder.getDirection(), "staffProfile.fullName"));
        }

        Specification<UserAccount> spec = (root, query, cb) -> {
            Join<UserAccount, StaffProfile> profileJoin = root.join("staffProfile");
            return cb.equal(root.get("role"), UserAccount.Role.RECEPTIONIST);
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

    public StaffProfileDto convertToStaffProfileDto(UserAccount account) {
        StaffProfile profile = staffProfileRepository.findById(account.getUserId()).orElse(new StaffProfile());
        StaffProfileDto dto = new StaffProfileDto();
        dto.setUserId(account.getUserId());
        dto.setUsername(account.getUsername());
        dto.setFullName(profile.getFullName());
        dto.setActive(account.isActive());
        if (profile.getBranch() != null) {
            dto.setBranchName(profile.getBranch().getName());
        }
        return dto;
    }


    @Override
    public StaffProfileDto findReceptionistById(Integer id) {
        UserAccount account = userAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Receptionist not found with ID: " + id));

        return convertToStaffProfileDto(account);
    }



    @Override
    @Transactional
    public void updateReceptionist(Integer userId, AdminUpdateRequestDto dto) {
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
    public void deleteReceptionist(Integer userId) {
        userAccountRepository.deleteById(userId);
    }


}