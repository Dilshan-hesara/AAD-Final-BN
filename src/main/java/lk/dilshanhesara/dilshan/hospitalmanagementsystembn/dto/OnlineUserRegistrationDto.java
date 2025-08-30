package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class OnlineUserRegistrationDto {
    private String fullName;
    private String username;
    private String email;
    private String contactNumber;
    private String password;
    // Patient Details
    private LocalDate dateOfBirth;
    private String gender;
    private String address;
}