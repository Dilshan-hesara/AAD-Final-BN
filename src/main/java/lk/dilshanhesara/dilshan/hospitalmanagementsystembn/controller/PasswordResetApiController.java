package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.controller;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/password-reset")
@RequiredArgsConstructor
public class PasswordResetApiController {
    private final OtpService otpService;

    @PostMapping("/request")
    public ResponseEntity<?> requestReset(@RequestBody Map<String, String> payload) {
        try {
            // --- CRITICAL CHANGE: Use the correct key from the frontend ---
            otpService.generateAndSendOtp(payload.get("identifier"));
            return ResponseEntity.ok(Map.of("message", "OTP sent successfully."));
        } catch (RuntimeException e) {
            // If the user is not found, return a 404 Not Found status
            return ResponseEntity.status(404).body(Map.of("error", "User not found with the provided identifier."));
        }
    }
    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> payload) {
        boolean isValid = otpService.verifyOtp(payload.get("username"), payload.get("otp"));
        return isValid ? ResponseEntity.ok("OTP verified.") : ResponseEntity.badRequest().body("Invalid or expired OTP.");
    }
    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> payload) {
        // Here you should re-verify the OTP or use a temporary token for security
        otpService.resetPassword(payload.get("username"), payload.get("newPassword"));
        return ResponseEntity.ok("Password has been reset successfully.");
    }
}