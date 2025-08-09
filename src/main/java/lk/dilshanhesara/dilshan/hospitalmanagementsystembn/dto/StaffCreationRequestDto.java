package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto;


import lombok.Data;

@Data
public class StaffCreationRequestDto {
    private String fullName;
    private String username;
    private String password;
    private String role;
    private Long branchId;
    private String email;
    private String contactNumber;
}