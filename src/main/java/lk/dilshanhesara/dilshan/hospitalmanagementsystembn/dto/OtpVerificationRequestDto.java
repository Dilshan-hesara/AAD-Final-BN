package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto;

import lombok.Data;

@Data
public class OtpVerificationRequestDto {
    private String username;
    private String otpCode;
}