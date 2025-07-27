package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto;

import lombok.Data;

@Data
public class DoctorDto {
    private int id;
    private String fullName;
    private String email;
    private String contactNumber;
    private String specialization;
    private Long branchId; // Used when creating/updating
    private String status;
}