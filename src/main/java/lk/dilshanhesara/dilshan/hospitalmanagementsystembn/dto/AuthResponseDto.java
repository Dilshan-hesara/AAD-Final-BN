package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor // Lombok constructor for all fields
public class AuthResponseDto {
    private boolean success;
    private String message;
    private String redirectUrl;
    private String role; // <-- අලුතින් එකතු කළ කොටස

}