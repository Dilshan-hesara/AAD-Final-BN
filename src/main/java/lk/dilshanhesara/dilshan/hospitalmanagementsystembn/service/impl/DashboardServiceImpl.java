package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.impl;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Appointment;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.AppointmentRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.DoctorRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.PatientRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.UserAccountRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;


    @Autowired
    private UserAccountRepository userAccountRepository;


@Override
public Map<String, Object> getBranchStatistics(Long branchId) {
    LocalDateTime startOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
    LocalDateTime endOfDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

    long receptionistCount = userAccountRepository.countReceptionistsByBranch(branchId);
    long activeDoctorCount = doctorRepository.countByBranch_IdAndStatus(branchId, "ACTIVE");
    long confirmedCount = appointmentRepository.countByBranch_IdAndStatusAndAppointmentDateBetween(branchId, "CONFIRMED", startOfDay, endOfDay);
    long completedCount = appointmentRepository.countByBranch_IdAndStatusAndAppointmentDateBetween(branchId, "COMPLETED", startOfDay, endOfDay);
    long cancelledCount = appointmentRepository.countByBranch_IdAndStatusAndAppointmentDateBetween(branchId, "CANCELLED", startOfDay, endOfDay);
    long onlineAppointments = appointmentRepository.countTodaysOnlineAppointments(branchId, startOfDay, endOfDay);
    long walkInAppointments = appointmentRepository.countTodaysWalkInAppointments(branchId, startOfDay, endOfDay);

    List<Appointment> completedAppointments = appointmentRepository.findByBranch_IdAndStatusAndAppointmentDateBetween(branchId, "COMPLETED", startOfDay, endOfDay);
    double todaysEarnings = completedAppointments.stream().mapToDouble(app -> app.getFee() != null ? app.getFee().doubleValue() : 0.0).sum();

    Map<String, Object> stats = new HashMap<>();
    stats.put("receptionistCount", receptionistCount);
    stats.put("doctorCount", activeDoctorCount);
    stats.put("confirmedCount", confirmedCount);
    stats.put("completedCount", completedCount);
    stats.put("cancelledCount", cancelledCount);
    stats.put("onlineAppointments", onlineAppointments);
    stats.put("walkInAppointments", walkInAppointments);
    stats.put("todaysEarnings", todaysEarnings);

    return stats;
}
}