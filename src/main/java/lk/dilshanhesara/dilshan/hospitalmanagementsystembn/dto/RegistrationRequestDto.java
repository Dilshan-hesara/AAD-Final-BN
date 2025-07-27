package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationRequestDto {

    @NotBlank(message = "Full name cannot be empty")
    private String fullName;

    @NotBlank(message = "Username cannot be empty")
    private String username;

    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Contact number cannot be empty")
    private String contactNumber;

    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;
}