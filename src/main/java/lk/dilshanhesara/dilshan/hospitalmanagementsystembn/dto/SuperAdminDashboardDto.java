package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SuperAdminDashboardDto {
    // User Counts
    private long totalPatients;
    private long totalDoctors;
    private long totalReceptionists;
    private long totalBranchAdmins;
    private long totalSuperAdmins;

    // Active User Counts
    private long activeDoctors;
    private long activeReceptionists;
    private long activeBranchAdmins;

    // Monthly Appointment Counts
    private long appointmentsThisMonth;
    private long onlineAppointmentsThisMonth;
    private long cancelledAppointmentsThisMonth;

    // Financials
    private double revenueThisMonth;


    private long totalBranches;

    public SuperAdminDashboardDto(long totalBranches, long totalPatients, double revenueThisMonth, long totalDoctors, long activeDoctors, long totalReceptionists, long activeReceptionists, long totalBranchAdmins, long activeBranchAdmins, long totalSuperAdmins, long appointmentsThisMonth, long onlineAppointmentsThisMonth, long cancelledAppointmentsThisMonth) {

        this.totalBranches = totalBranches;
        this.totalPatients = totalPatients;
        this.revenueThisMonth = revenueThisMonth;
        this.totalDoctors = totalDoctors;
        this.activeDoctors = activeDoctors;
        this.totalReceptionists = totalReceptionists;
        this.activeReceptionists = activeReceptionists;
        this.totalBranchAdmins = totalBranchAdmins;
        this.activeBranchAdmins = activeBranchAdmins;
        this.totalSuperAdmins = totalSuperAdmins;
        this.appointmentsThisMonth = appointmentsThisMonth;
        this.onlineAppointmentsThisMonth = onlineAppointmentsThisMonth;
        this.cancelledAppointmentsThisMonth = cancelledAppointmentsThisMonth;
    }
}