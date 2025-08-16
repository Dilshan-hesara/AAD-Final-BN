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
    private final PatientRepository patientRepository; // Assume this has a creation_date field

//    @Override
//    public AdvancedBranchDashboardDto getAdvancedDashboardSummary(Long branchId, LocalDateTime startDate, LocalDateTime endDate) {
//        return null;
//    }

//    @Override
//    public AdvancedBranchDashboardDto getAdvancedDashboardSummary(Long branchId, LocalDateTime startDate, LocalDateTime endDate) {
//
//        // Revenue from completed appointments in the date range
//        List<Appointment> completed = appointmentRepository.findByBranch_IdAndStatusAndAppointmentDateBetween(branchId, "COMPLETED", startDate, endDate);
//        double revenue = completed.stream().mapToDouble(app -> app.getFee().doubleValue()).sum();
//
//        // Other stats
//        long newPatientCount = patientRepository.countByBranchAndCreatedAtBetween(branchId, startDate, endDate); // You'd need a way to link patients to a branch
//        long confirmedAppointments = appointmentRepository.countByBranch_IdAndStatusAndAppointmentDateBetween(branchId, "CONFIRMED", startDate, endDate);
//        long completedAppointments = completed.size();
//        long onlineBookings = appointmentRepository.countTodaysOnlineAppointments(branchId, startDate, endDate);
//        long walkInBookings = appointmentRepository.countTodaysWalkInAppointments(branchId, startDate, endDate);
//
//        // This is complex logic that would be implemented to get chart/table data
//        List<TopDoctorDto> topDoctors = List.of(new TopDoctorDto("Dr. Perera", 15));
//        List<DailyAppointmentStatDto> appointmentTrend = List.of(new DailyAppointmentStatDto("Mon", 5), new DailyAppointmentStatDto("Tue", 8));
//
//        AdvancedBranchDashboardDto dto = new AdvancedBranchDashboardDto();
//        dto.setRevenue(revenue);
//        dto.setNewPatientCount(newPatientCount);
//        // ... set all other fields
//
//        return dto;
//    }

//
////
//    @Override
//    public AdvancedBranchDashboardDto getAdvancedDashboardSummary(Long branchId, LocalDateTime startDate, LocalDateTime endDate) {
//
//        List<Appointment> completed = appointmentRepository.findByBranch_IdAndStatusAndAppointmentDateBetween(branchId, "COMPLETED", startDate, endDate);
//        double revenue = completed.stream().mapToDouble(app -> app.getFee() != null ? app.getFee().doubleValue() : 0.0).sum();
//
//        long newPatientCount = patientRepository.countNewPatientsByBranchAndDateRange(branchId, startDate, endDate);
//        long confirmedAppointments = appointmentRepository.countByBranch_IdAndStatusAndAppointmentDateBetween(branchId, "CONFIRMED", startDate, endDate);
//        long completedAppointments = completed.size();
//        long onlineBookings = appointmentRepository.countTodaysOnlineAppointments(branchId, startDate, endDate);
//        long walkInBookings = appointmentRepository.countTodaysWalkInAppointments(branchId, startDate, endDate);
//
//        List<TopDoctorDto> topDoctors = appointmentRepository.findTopDoctorsByBranch(branchId, startDate, endDate);
//        List<DailyAppointmentStatDto> appointmentTrend = appointmentRepository.findDailyAppointmentStats(branchId, startDate, endDate);
//
//        AdvancedBranchDashboardDto dto = new AdvancedBranchDashboardDto();
//        dto.setRevenue(revenue);
//        dto.setNewPatientCount(newPatientCount);
//        dto.setConfirmedAppointments(confirmedAppointments);
//        dto.setCompletedAppointments(completedAppointments);
//        dto.setOnlineBookings(onlineBookings);
//        dto.setWalkInBookings(walkInBookings);
//        dto.setTopDoctors(topDoctors);
//        dto.setAppointmentTrend(appointmentTrend);
//
//        return dto;
//    }

    @Override
    public AdvancedBranchDashboardDto getAdvancedDashboardSummary(Long branchId, LocalDateTime startDate, LocalDateTime endDate) {

        // Completed appointments & revenue calculation
        List<Appointment> completed = appointmentRepository
                .findByBranch_IdAndStatusAndAppointmentDateBetween(branchId, "COMPLETED", startDate, endDate);

        double revenue = completed.stream()
                .mapToDouble(app -> app.getFee() != null ? app.getFee().doubleValue() : 0.0)
                .sum();

        // Other stats
        long newPatientCount = patientRepository.countNewPatientsByBranchAndDateRange(branchId, startDate, endDate);
        long confirmedAppointments = appointmentRepository.countByBranch_IdAndStatusAndAppointmentDateBetween(branchId, "CONFIRMED", startDate, endDate);
        long completedAppointments = completed.size();
        long onlineBookings = appointmentRepository.countTodaysOnlineAppointments(branchId, startDate, endDate);
        long walkInBookings = appointmentRepository.countTodaysWalkInAppointments(branchId, startDate, endDate);

        // Top doctors
        List<TopDoctorDto> topDoctors = appointmentRepository.findTopDoctorsByBranch(branchId, startDate, endDate);

        // Appointment trend (raw Object[] → DTO mapping)
        List<Object[]> rawStats = appointmentRepository.findDailyAppointmentStats(branchId, startDate, endDate);

        List<DailyAppointmentStatDto> appointmentTrend = rawStats.stream()
                .map(obj -> new DailyAppointmentStatDto(
                        String.valueOf((LocalDate) obj[0]),    // Date
                        (Long) obj[1]          // Count
                ))
                .toList();

        // Build DTO
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
