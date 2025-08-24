package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.impl;

import jakarta.persistence.criteria.Join;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.AdminUpdateRequestDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.StaffCreationRequestDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.StaffProfileDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.StaffProfile;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.UserAccount;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.StaffProfileRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.UserAccountRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.FileStorageService;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.SuperAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class SuperAdminServiceImpl implements SuperAdminService {

    private final UserAccountRepository userAccountRepository;
    private final StaffProfileRepository staffProfileRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<StaffProfileDto> searchSuperAdmins(String keyword, Pageable pageable) {
        Specification<UserAccount> spec = (root, query, cb) -> {
            return cb.equal(root.get("role"), UserAccount.Role.SUPER_ADMIN);
        };

        if (keyword != null && !keyword.isEmpty()) {
            spec = spec.and((root, query, cb) -> {
                Join<UserAccount, StaffProfile> profileJoin = root.join("staffProfile");
                return cb.like(profileJoin.get("fullName"), "%" + keyword + "%");
            });
        }

        Page<UserAccount> accounts = userAccountRepository.findAll(spec, pageable);
        return accounts.map(this::convertToStaffProfileDto);
    }

    @Override
    public StaffProfileDto findSuperAdminById(Integer userId) {
        UserAccount account = userAccountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Super Admin not found"));
        return convertToStaffProfileDto(account);
    }



    @Override
    public void deleteSuperAdmin(Integer userId, String currentUsername) {
        UserAccount accountToDelete = userAccountRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (accountToDelete.getUsername().equals(currentUsername)) {
            throw new IllegalArgumentException("You cannot delete your own account.");
        }
        userAccountRepository.deleteById(userId);
    }

    @Override
    public void updateUserStatus(Integer userId, boolean isActive, String currentUsername) {
        UserAccount account = userAccountRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        if (account.getUsername().equals(currentUsername)) {
            throw new IllegalArgumentException("You cannot deactivate your own account.");
        }
        account.setActive(isActive);
        userAccountRepository.save(account);
    }


    private final FileStorageService fileStorageService;




    private StaffProfileDto convertToStaffProfileDto(UserAccount account, StaffProfile profile) {
        StaffProfileDto dto = new StaffProfileDto();
        dto.setUserId(account.getUserId());
        dto.setUsername(account.getUsername());
        dto.setFullName(profile.getFullName());
        dto.setEmail(profile.getEmail());
        dto.setContactNumber(profile.getContactNumber());
        dto.setProfilePictureUrl(profile.getProfilePictureUrl());
        return dto;
    }




    @Override
    @Transactional
    public void addSuperAdmin(StaffCreationRequestDto dto) {
        if (userAccountRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        UserAccount account = new UserAccount();
        account.setUsername(dto.getUsername());
        account.setPassword(passwordEncoder.encode(dto.getPassword()));
        account.setRole(UserAccount.Role.SUPER_ADMIN);
        account.setActive(true);
        account = userAccountRepository.save(account);

        StaffProfile profile = new StaffProfile();
        profile.setUserAccount(account);
        profile.setFullName(dto.getFullName());
        profile.setEmail(dto.getEmail());
        profile.setContactNumber(dto.getContactNumber());
        profile.setBranch(null);
        staffProfileRepository.save(profile);
    }

    @Override
    @Transactional
    public void updateSuperAdmin(Integer userId, AdminUpdateRequestDto dto) {
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
    public StaffProfileDto findSuperAdminByUsername(String username) {
        UserAccount account = userAccountRepository.findByUsername(username).orElseThrow();
        return convertToStaffProfileDto(account);
    }

    @Override
    public void updateMyProfile(String username, AdminUpdateRequestDto dto) {
        UserAccount account = userAccountRepository.findByUsername(username).orElseThrow();
        updateSuperAdmin(account.getUserId(), dto);
    }

    @Override
    public void updateProfilePicture(String username, MultipartFile file) {
        UserAccount account = userAccountRepository.findByUsername(username).orElseThrow();
        StaffProfile profile = staffProfileRepository.findById(account.getUserId()).orElseThrow();

        String filePath = fileStorageService.storeFile(file);
        profile.setProfilePictureUrl(filePath);
        staffProfileRepository.save(profile);
    }





    private StaffProfileDto convertToStaffProfileDto(UserAccount account) {
        StaffProfile profile = staffProfileRepository.findById(account.getUserId())
                .orElseThrow(() -> new RuntimeException("Staff profile not found for user: " + account.getUsername()));

        StaffProfileDto dto = new StaffProfileDto();

        dto.setUserId(account.getUserId());
        dto.setUsername(account.getUsername());
        dto.setRole(account.getRole().name());
        dto.setActive(account.isActive());

        dto.setFullName(profile.getFullName());
        dto.setEmail(profile.getEmail());
        dto.setContactNumber(profile.getContactNumber());
        dto.setProfilePictureUrl(profile.getProfilePictureUrl());

        return dto;
    }

























}