package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.impl;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.OnlineUserRegistrationDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.PasswordChangeDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.UserProfileDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.OnlineUserProfile;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Patient;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.UserAccount;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.OnlineUserProfileRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.PatientRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.UserAccountRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.FileStorageService;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.OnlineUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class OnlineUserServiceImpl implements OnlineUserService {

    private final UserAccountRepository userAccountRepository;
    private final OnlineUserProfileRepository onlineUserProfileRepository;

    @Override
    public UserProfileDto findProfileByUsername(String username) {
        UserAccount account = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User account not found for username: " + username));

        OnlineUserProfile profile = onlineUserProfileRepository.findById(account.getUserId())
                .orElseThrow(() -> new RuntimeException("Online user profile not found for user ID: " + account.getUserId()));

        UserProfileDto dto = new UserProfileDto();
        dto.setUserId(account.getUserId());
        dto.setUsername(account.getUsername());
        dto.setRole(account.getRole().name());
        dto.setFullName(profile.getFullName());
        dto.setEmail(profile.getEmail());
        dto.setContactNumber(profile.getContactNumber());
        dto.setProfilePictureUrl(profile.getProfilePictureUrl());

        return dto;
    }

    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void registerNewOnlineUser(OnlineUserRegistrationDto dto) {
        if (userAccountRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        UserAccount account = new UserAccount();
        account.setUsername(dto.getUsername());
        account.setPassword(passwordEncoder.encode(dto.getPassword()));
        account.setRole(UserAccount.Role.ONLINEUSER);
        account.setActive(true);
        account = userAccountRepository.save(account);

        OnlineUserProfile profile = new OnlineUserProfile();
        profile.setUserAccount(account);
        profile.setFullName(dto.getFullName());
        profile.setEmail(dto.getEmail());
        profile.setContactNumber(dto.getContactNumber());
        onlineUserProfileRepository.save(profile);

        Patient patient = new Patient();
        patient.setFullName(dto.getFullName());
        patient.setEmail(dto.getEmail());
        patient.setContactNumber(dto.getContactNumber());
        patient.setDateOfBirth(dto.getDateOfBirth());
        patient.setGender(dto.getGender());
        patient.setAddress(dto.getAddress());
        patient.setLinkedOnlineUser(account);
        patientRepository.save(patient);
    }


    private final OnlineUserProfileRepository profileRepository;

    @Override
    public void updateMyProfile(String username, UserProfileDto dto) {
        OnlineUserProfile profile = profileRepository.findByUserAccount_Username(username).orElseThrow();
        profile.setFullName(dto.getFullName());
        profile.setEmail(dto.getEmail());
        profile.setContactNumber(dto.getContactNumber());
        profileRepository.save(profile);
    }


}