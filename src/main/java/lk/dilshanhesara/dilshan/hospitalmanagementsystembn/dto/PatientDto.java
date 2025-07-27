package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientDto {
    private Long id; // This is the patient record ID
    private String fullName;
    private String email;
    private String contactNumber;
    private LocalDate dateOfBirth;
    private String gender;
    private Integer linkedOnlineUserId; // To know if they have an online account
}