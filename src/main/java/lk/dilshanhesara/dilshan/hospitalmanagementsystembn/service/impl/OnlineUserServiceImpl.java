package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.impl;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.UserProfileDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.OnlineUserProfile;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.UserAccount;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.OnlineUserProfileRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.UserAccountRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.OnlineUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OnlineUserServiceImpl implements OnlineUserService {

    private final UserAccountRepository userAccountRepository;
    private final OnlineUserProfileRepository onlineUserProfileRepository;

    @Override
    public UserProfileDto findProfileByUsername(String username) {
        // Find the main user account by username
        UserAccount account = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User account not found for username: " + username));

        // Find the linked online user profile using the user_id
        OnlineUserProfile profile = onlineUserProfileRepository.findById(account.getUserId())
                .orElseThrow(() -> new RuntimeException("Online user profile not found for user ID: " + account.getUserId()));

        // Create a DTO to send the combined data to the frontend
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
}