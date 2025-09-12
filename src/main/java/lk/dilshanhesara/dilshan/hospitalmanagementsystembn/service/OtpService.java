package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.OtpCode;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.UserAccount;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.OtpCodeRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

//... imports
@Service
@RequiredArgsConstructor
public class OtpService {
    private final OtpCodeRepository otpCodeRepository;
    private final UserAccountRepository userAccountRepository;
    private final EmailService emailService;
    // ... other services
    @Transactional
    public void generateAndSendOtp(String username) {
        UserAccount account = userAccountRepository.findByUsername(username).orElseThrow();
        // ... Logic to get the user's email from their profile ...
        String otp = new Random().ints(6, 0, 10).mapToObj(String::valueOf).collect(Collectors.joining());
        OtpCode otpCode = new OtpCode();
        otpCode.setUserAccount(account);
        otpCode.setOtpCode(otp);
        otpCode.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        otpCodeRepository.save(otpCode);
        emailService.sendOtpEmail(userEmail, otp);
    }
    public boolean verifyOtp(String username, String code) {
        // ... Logic to verify the OTP
    }
    public void resetPassword(String username, String newPassword) {
        // ... Logic to find user and update password
    }
}