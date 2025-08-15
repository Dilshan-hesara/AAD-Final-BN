package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto;

import lombok.Data;

@Data
public class BranchDto {
    private Long id;
    private String name;

    private String location;
    private String contactNumber;
    private String email;
    private String status;
}