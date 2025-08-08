package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.impl;


import jakarta.transaction.Transactional;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.RegistrationRequestDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.OnlineUserProfile;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.UserAccount;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.OnlineUserProfileRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.UserAccountRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserAccountRepository userAccountRepository;
    private final OnlineUserProfileRepository onlineUserProfileRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional // Ensures both saves happen or neither do
    public void registerOnlineUser(RegistrationRequestDto dto) {
        if (userAccountRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        // 1. Create and save the UserAccount
        UserAccount account = new UserAccount();
        account.setUsername(dto.getUsername());
        account.setPassword(passwordEncoder.encode(dto.getPassword()));
        account.setRole(UserAccount.Role.ONLINEUSER);
        account.setActive(true); // Or false until OTP verification
        account = userAccountRepository.save(account);

        // 2. Create and save the OnlineUserProfile, linking the account
        OnlineUserProfile profile = new OnlineUserProfile();
        profile.setUserAccount(account);
        profile.setFullName(dto.getFullName());
        profile.setEmail(dto.getEmail());
        profile.setContactNumber(dto.getContactNumber());
        onlineUserProfileRepository.save(profile);
    }
}