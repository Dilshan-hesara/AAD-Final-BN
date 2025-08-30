package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OnlineUserAppointmentRequestDto {
    private Integer doctorId;
    private Long branchId;
    private LocalDateTime appointmentDate;
    private String reason;
}
