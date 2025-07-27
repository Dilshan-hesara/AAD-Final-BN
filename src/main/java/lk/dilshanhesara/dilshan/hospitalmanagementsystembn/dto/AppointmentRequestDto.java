package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AppointmentRequestDto {
    private Long patientId; // This would be created on the backend for the logged-in user
    private Long doctorId;
    private Long branchId;
    private LocalDateTime appointmentDate;
    private String reason;
}