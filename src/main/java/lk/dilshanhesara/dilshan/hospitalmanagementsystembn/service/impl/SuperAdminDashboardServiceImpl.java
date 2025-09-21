package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.impl;


import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.SuperAdminDashboardDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.UserAccount;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.*;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.SuperAdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class SuperAdminDashboardServiceImpl implements SuperAdminDashboardService {

    private final UserAccountRepository userAccountRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final BranchRepository branchRepository;



    @Override
    public SuperAdminDashboardDto getDashboardSummary() {
        YearMonth currentMonth = YearMonth.now();
        LocalDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = currentMonth.atEndOfMonth().atTime(LocalTime.MAX);

        long totalBranches = branchRepository.count();
        long totalPatients = patientRepository.count();
        long totalDoctors = doctorRepository.count();
        long activeDoctors = doctorRepository.countByStatus("ACTIVE");
        long totalReceptionists = userAccountRepository.countByRole(UserAccount.Role.RECEPTIONIST);
        long activeReceptionists = userAccountRepository.countByRoleAndIsActive(UserAccount.Role.RECEPTIONIST, true);
        long totalBranchAdmins = userAccountRepository.countByRole(UserAccount.Role.BRANCH_ADMIN);
        long activeBranchAdmins = userAccountRepository.countByRoleAndIsActive(UserAccount.Role.BRANCH_ADMIN, true);
        long totalSuperAdmins = userAccountRepository.countByRole(UserAccount.Role.SUPER_ADMIN);
        long appointmentsThisMonth = appointmentRepository.countByAppointmentDateBetween(startOfMonth, endOfMonth);
        long onlineAppointmentsThisMonth = appointmentRepository.countOnlineAppointmentsBetween(startOfMonth, endOfMonth);
        long cancelledAppointmentsThisMonth = appointmentRepository.countByStatusAndAppointmentDateBetween("CANCELLED", startOfMonth, endOfMonth);
        double revenueThisMonth = 550000.00;

        return new SuperAdminDashboardDto(
                totalBranches, totalPatients, revenueThisMonth,
                totalDoctors, activeDoctors, totalReceptionists, activeReceptionists,
                totalBranchAdmins, activeBranchAdmins, totalSuperAdmins,
                appointmentsThisMonth, onlineAppointmentsThisMonth, cancelledAppointmentsThisMonth
        );
    }


}