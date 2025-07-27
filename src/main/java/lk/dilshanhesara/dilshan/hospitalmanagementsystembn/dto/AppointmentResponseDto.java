package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AppointmentResponseDto {
    private Long id;
    private String patientName;
    private String doctorName;
    private String branchName;
    private LocalDateTime appointmentDate;
    private String status;
}