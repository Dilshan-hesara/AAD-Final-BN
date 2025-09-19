package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.impl;


import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.PasswordChangeDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.StaffProfileDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.StaffProfile;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.UserAccount;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.StaffProfileRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.UserAccountRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.StaffProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class StaffProfileServiceImpl implements StaffProfileService {

    private final UserAccountRepository userAccountRepository;
    private final StaffProfileRepository staffProfileRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public StaffProfileDto findStaffByUsername(String username) {
        UserAccount account = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User account not found for username: " + username));

        Optional<StaffProfile> profileOptional = staffProfileRepository.findById(account.getUserId());

        if (profileOptional.isEmpty()) {

            StaffProfileDto dto = new StaffProfileDto();
            dto.setUserId(account.getUserId());
            dto.setUsername(account.getUsername());
            dto.setRole(account.getRole().name());
            return dto;
        }

        StaffProfile profile = profileOptional.get();
        StaffProfileDto dto = new StaffProfileDto();
        dto.setUserId(profile.getUserId());
        dto.setFullName(profile.getFullName());
        dto.setUsername(account.getUsername());
        dto.setRole(account.getRole().name());

        if (profile.getBranch() != null) {
            dto.setBranchId(profile.getBranch().getId());
            dto.setBranchName(profile.getBranch().getName());
        }

        return dto;
    }



    @Override
    public StaffProfileDto getCurrentLoggedInStaffProfile() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return findStaffByUsername(username);
    }








    @Override
    public void updateProfile(String username, StaffProfileDto profileDto) {
        UserAccount account = userAccountRepository.findByUsername(username).orElseThrow();
        StaffProfile profile = staffProfileRepository.findById(account.getUserId()).orElseThrow();

        profile.setFullName(profileDto.getFullName());
        profile.setEmail(profileDto.getEmail());
        profile.setContactNumber(profileDto.getContactNumber());

        staffProfileRepository.save(profile);
    }




    @Override
    public void changePassword(String username, PasswordChangeDto passwordDto) {
        UserAccount account = userAccountRepository.findByUsername(username).orElseThrow();

        if (!passwordEncoder.matches(passwordDto.getCurrentPassword(), account.getPassword())) {
            throw new RuntimeException("Incorrect current password");
        }

        account.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
        userAccountRepository.save(account);
    }




    public StaffProfileDto convertToStaffProfileDto(UserAccount account) {
        StaffProfile profile = staffProfileRepository.findById(account.getUserId())
                .orElse(new StaffProfile());

        StaffProfileDto dto = new StaffProfileDto();

        dto.setUserId(account.getUserId());
        dto.setUsername(account.getUsername());
        dto.setActive(account.isActive());
        dto.setRole(account.getRole().name());

        dto.setFullName(profile.getFullName());
        dto.setEmail(profile.getEmail());
        dto.setContactNumber(profile.getContactNumber());

        dto.setProfilePictureUrl(profile.getProfilePictureUrl());

        if (profile.getBranch() != null) {
            dto.setBranchName(profile.getBranch().getName());
        }

        return dto;
    }
}