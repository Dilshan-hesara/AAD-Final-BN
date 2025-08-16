package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor // <-- ADD THIS ANNOTATION
@AllArgsConstructor
public class AdvancedBranchDashboardDto {
    private double revenue;
    private long newPatientCount;
    private long confirmedAppointments;
    private long completedAppointments;
    private long onlineBookings;
    private long walkInBookings;
    private List<TopDoctorDto> topDoctors; // New DTO for top doctors
    private List<DailyAppointmentStatDto> appointmentTrend; // New DTO for line chart




}