package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto;


import lombok.Data;

@Data
public class UserProfileDto {
    private Integer userId;
    private String username;
    private String fullName;
    private String email;
    private String contactNumber;
    private String profilePictureUrl;
    private String role;
}