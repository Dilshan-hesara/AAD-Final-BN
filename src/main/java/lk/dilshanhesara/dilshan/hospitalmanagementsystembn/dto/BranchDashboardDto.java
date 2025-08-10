package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BranchDashboardDto {
    private String branchName;
    private String location;
    private String contactNumber;
    private Long patientCount;
    private Long doctorCount;
    private Long appointmentsToday;
    private Long receptionistCount; //
    private Long confirmedCount;
    private Long completedCount;
    private Long cancelledCount;
    private Long totalAppointmentsToday;

    private Double todaysEarnings;

    private Long onlineAppointments;
    private Long walkInAppointments;

    public BranchDashboardDto(String name, String location, String contactNumber, Long receptionistCount, Long doctorCount, Long appointmentsToday) {

        this.branchName = name;
        this.location = location;
        this.contactNumber = contactNumber;
        this.receptionistCount = receptionistCount;
        this.doctorCount = doctorCount;
        this.appointmentsToday = appointmentsToday;

    }


    public BranchDashboardDto(String name, String location, String contactNumber, Long receptionistCount, Long doctorCount, Long confirmedCount, Long completedCount, Long cancelledCount, Double todaysEarnings) {

        this.branchName = name;
        this.location = location;
        this.contactNumber = contactNumber;
        this.receptionistCount = receptionistCount;
        this.doctorCount = doctorCount;
        this.confirmedCount = confirmedCount;
        this.completedCount = completedCount;
        this.cancelledCount = cancelledCount;
        this.todaysEarnings = todaysEarnings;
        this.appointmentsToday = confirmedCount + completedCount + cancelledCount; // Assuming appointmentsToday is the sum of all appointment statuses
    }

    public BranchDashboardDto(String name, String location, String contactNumber, Long receptionistCount, Long doctorCount, Long confirmedCount, Long completedCount, Long cancelledCount, Long onlineAppointments, Long walkInAppointments, Double todaysEarnings) {


        this.branchName = name;
        this.location = location;
        this.contactNumber = contactNumber;
        this.receptionistCount = receptionistCount;
        this.doctorCount = doctorCount;
        this.confirmedCount = confirmedCount;
        this.completedCount = completedCount;
        this.cancelledCount = cancelledCount;
        this.onlineAppointments = onlineAppointments;
        this.walkInAppointments = walkInAppointments;
        this.todaysEarnings = todaysEarnings;

    }
}