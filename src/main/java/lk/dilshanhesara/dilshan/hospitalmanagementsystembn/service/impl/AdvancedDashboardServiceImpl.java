package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.impl;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.AdvancedBranchDashboardDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.DailyAppointmentStatDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.TopDoctorDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Appointment;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.AppointmentRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.PatientRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.AdvancedDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AdvancedDashboardServiceImpl implements AdvancedDashboardService {


    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;

    @Override
    public AdvancedBranchDashboardDto getAdvancedDashboardSummary(Long branchId, LocalDateTime startDate, LocalDateTime endDate) {

        List<Appointment> completed = appointmentRepository
                .findByBranch_IdAndStatusAndAppointmentDateBetween(branchId, "COMPLETED", startDate, endDate);

        double revenue = completed.stream()
                .mapToDouble(app -> app.getFee() != null ? app.getFee().doubleValue() : 0.0)
                .sum();

        long newPatientCount = patientRepository.countNewPatientsByBranchAndDateRange(branchId, startDate, endDate);
        long confirmedAppointments = appointmentRepository.countByBranch_IdAndStatusAndAppointmentDateBetween(branchId, "CONFIRMED", startDate, endDate);
        long completedAppointments = completed.size();
        long onlineBookings = appointmentRepository.countTodaysOnlineAppointments(branchId, startDate, endDate);
        long walkInBookings = appointmentRepository.countTodaysWalkInAppointments(branchId, startDate, endDate);

        List<TopDoctorDto> topDoctors = appointmentRepository.findTopDoctorsByBranch(branchId, startDate, endDate);

        List<Object[]> rawStats = appointmentRepository.findDailyAppointmentStats(branchId, startDate, endDate);

        List<DailyAppointmentStatDto> appointmentTrend = rawStats.stream()
                .map(obj -> new DailyAppointmentStatDto(
                        String.valueOf((LocalDate) obj[0]),
                        (Long) obj[1]
                ))
                .toList();

        AdvancedBranchDashboardDto dto = new AdvancedBranchDashboardDto();
        dto.setRevenue(revenue);
        dto.setNewPatientCount(newPatientCount);
        dto.setConfirmedAppointments(confirmedAppointments);
        dto.setCompletedAppointments(completedAppointments);
        dto.setOnlineBookings(onlineBookings);
        dto.setWalkInBookings(walkInBookings);
        dto.setTopDoctors(topDoctors);
        dto.setAppointmentTrend(appointmentTrend);

        return dto;
    }


}
