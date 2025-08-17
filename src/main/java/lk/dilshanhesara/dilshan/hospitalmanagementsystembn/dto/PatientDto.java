package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientDto {
    private Long id;
    private String fullName;
    private String email;
    private String contactNumber;
    private LocalDate dateOfBirth;
    private String gender;
    private Integer linkedOnlineUserId;
    private String address;
    private String branchName;

}