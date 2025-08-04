package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto;


import jakarta.persistence.Column;
import lombok.Data;

@Data
public class StaffProfileDto {
    private Integer userId;
    private String fullName;
    private String username;
    private String role;
    private Long branchId;
    private String branchName;
    private String email;

    private String contactNumber;
}