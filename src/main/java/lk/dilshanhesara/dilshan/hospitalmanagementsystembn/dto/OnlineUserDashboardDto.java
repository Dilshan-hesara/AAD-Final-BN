package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto;

import lombok.Data;
import java.util.List;

@Data
public class OnlineUserDashboardDto {
    private String fullName;
    private String patientId;
    private String profilePictureUrl;
    private AppointmentResponseDto upcomingAppointment;
    private List<AppointmentResponseDto> recentActivity;
}