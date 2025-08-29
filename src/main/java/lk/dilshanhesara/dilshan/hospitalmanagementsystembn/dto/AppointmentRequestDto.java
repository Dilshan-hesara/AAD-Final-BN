package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AppointmentRequestDto {


    private Long patientId;
    private Integer doctorId;
    private LocalDateTime appointmentDate;
    private String reason;



}