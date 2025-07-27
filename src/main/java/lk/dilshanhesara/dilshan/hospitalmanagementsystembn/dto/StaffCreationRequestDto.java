package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto;


import lombok.Data;

@Data
public class StaffCreationRequestDto {
    private String fullName;
    private String username;
    private String password;
    private String role; // e.g., "BRANCH_ADMIN", "RECEPTIONIST"
    private Long branchId; // Can be null if creating a SUPER_ADMIN
}