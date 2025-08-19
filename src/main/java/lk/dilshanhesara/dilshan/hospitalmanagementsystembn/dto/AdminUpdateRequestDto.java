package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto;

import lombok.Data;

@Data
public class AdminUpdateRequestDto {
    private String fullName;
    private String username;
    private String email;
    private String contactNumber;
    private String password;
}