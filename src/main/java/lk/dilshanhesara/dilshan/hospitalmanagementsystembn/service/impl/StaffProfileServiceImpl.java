package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.impl;


import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.StaffProfileDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.StaffProfile;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.UserAccount;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.StaffProfileRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.UserAccountRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.StaffProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

//
//@Service
//@RequiredArgsConstructor
//public class StaffProfileServiceImpl implements StaffProfileService {
//
//    private final UserAccountRepository userAccountRepository;
//    private final StaffProfileRepository staffProfileRepository;
//
//    @Override
//    public StaffProfileDto findStaffByUsername(String username) {
//        UserAccount account = userAccountRepository.findByUsername(username).orElseThrow();
//        StaffProfile profile = staffProfileRepository.findById(account.getUserId()).orElseThrow();
//
//        StaffProfileDto dto = new StaffProfileDto();
//        dto.setFullName(profile.getFullName());
//
//
//        if (profile.getBranch() != null) {
//            dto.setBranchName(profile.getBranch().getName());
//        }
//
//        return dto;
//    }
//}
//



@Service
@RequiredArgsConstructor
public class StaffProfileServiceImpl implements StaffProfileService {

    private final UserAccountRepository userAccountRepository;
    private final StaffProfileRepository staffProfileRepository;

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
        // Get the username of the currently logged-in user from Spring Security
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Use the existing method to find and return the profile
        return findStaffByUsername(username);
    }
}