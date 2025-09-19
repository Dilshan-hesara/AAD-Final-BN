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

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserAccountRepository userAccountRepository;
    private final OnlineUserProfileRepository onlineUserProfileRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void registerOnlineUser(RegistrationRequestDto dto) {
        if (userAccountRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }


        UserAccount account = new UserAccount();
        account.setUsername(dto.getUsername());
        account.setPassword(passwordEncoder.encode(dto.getPassword()));
        account.setRole(UserAccount.Role.ONLINEUSER);
        account.setActive(true); // Or false until OTP verification
        account = userAccountRepository.save(account);

        OnlineUserProfile profile = new OnlineUserProfile();
        profile.setUserAccount(account);
        profile.setFullName(dto.getFullName());
        profile.setEmail(dto.getEmail());
        profile.setContactNumber(dto.getContactNumber());
        onlineUserProfileRepository.save(profile);
    }


    @Override
    @Transactional
    public void processOAuthPostLogin(String email, String fullName) {
        Optional<UserAccount> existUser = userAccountRepository.findByUsername(email);

        if (existUser.isEmpty()) {
            UserAccount newUser = new UserAccount();
            newUser.setUsername(email);
            newUser.setPassword(passwordEncoder.encode("OAUTH_USER_DUMMY_PASSWORD"));
            newUser.setRole(UserAccount.Role.ONLINEUSER);
            newUser.setActive(true);
            newUser = userAccountRepository.save(newUser);

            OnlineUserProfile profile = new OnlineUserProfile();
            profile.setUserAccount(newUser);
            profile.setFullName(fullName);
            profile.setEmail(email);
            profile.setContactNumber("N/A");
            onlineUserProfileRepository.save(profile);
        }
    }
}