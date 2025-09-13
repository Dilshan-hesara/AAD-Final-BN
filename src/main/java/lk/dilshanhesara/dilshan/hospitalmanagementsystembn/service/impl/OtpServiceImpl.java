package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.impl;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.OtpCode;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.UserAccount;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.OnlineUserProfileRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.OtpCodeRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.StaffProfileRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.UserAccountRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.EmailService;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final OtpCodeRepository otpCodeRepository;
    private final UserAccountRepository userAccountRepository;
    private final StaffProfileRepository staffProfileRepository;
    private final OnlineUserProfileRepository onlineUserProfileRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void generateAndSendOtp(String username) {
        UserAccount account = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with that username"));

        // --- Logic to find the user's email ---
        String userEmail = findUserEmail(account.getUserId(), account.getRole());
        if (userEmail == null) {
            throw new RuntimeException("Email not found for the user.");
        }

        // Generate a 6-digit OTP
        String otp = new Random().ints(6, 0, 10)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());

        OtpCode otpCode = new OtpCode();
        otpCode.setUserAccount(account);
        otpCode.setOtpCode(otp);
        otpCode.setExpiryTime(LocalDateTime.now().plusMinutes(5)); // OTP expires in 5 minutes
        otpCodeRepository.save(otpCode);

        // Send the OTP via email
        emailService.sendOtpEmail(userEmail, otp);
    }

    @Override
    public boolean verifyOtp(String username, String code) {
        return otpCodeRepository.findByUserAccount_UsernameAndOtpCode(username, code)
                .map(otpCode -> {
                    // Check if the OTP has expired
                    if (otpCode.getExpiryTime().isBefore(LocalDateTime.now())) {
                        return false; // Expired
                    }
                    // If valid, delete it so it can't be used again
                    otpCodeRepository.delete(otpCode);
                    return true;
                })
                .orElse(false); // Not found
    }

    @Override
    @Transactional
    public void resetPassword(String username, String newPassword) {
        UserAccount account = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Encode the new password before saving
        account.setPassword(passwordEncoder.encode(newPassword));
        userAccountRepository.save(account);
    }

    private String findUserEmail(Integer userId, UserAccount.Role role) {
        if (role == UserAccount.Role.ONLINEUSER) {
            return onlineUserProfileRepository.findById(userId).map(OnlineUserProfile::getEmail).orElse(null);
        } else {
            return staffProfileRepository.findById(userId).map(StaffProfile::getEmail).orElse(null);
        }
    }
}