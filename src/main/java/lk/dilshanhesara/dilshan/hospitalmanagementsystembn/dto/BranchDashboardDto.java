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

    public BranchDashboardDto(String name, String location, String contactNumber, Long receptionistCount, Long doctorCount, Long appointmentsToday) {

        this.branchName = name;
        this.location = location;
        this.contactNumber = contactNumber;
        this.receptionistCount = receptionistCount;
        this.doctorCount = doctorCount;
        this.appointmentsToday = appointmentsToday;

    }

    public BranchDashboardDto(String name, String location, String contactNumber, Long receptionistCount, Long doctorCount, Long appointmentsToday, Long confirmedCount, Long completedCount, Long cancelledCount, Long totalAppointmentsToday) {

        this.branchName = name;
        this.location = location;
        this.contactNumber = contactNumber;
        this.receptionistCount = receptionistCount;
        this.doctorCount = doctorCount;
        this.appointmentsToday = appointmentsToday;
        this.confirmedCount = confirmedCount;
        this.completedCount = completedCount;
        this.cancelledCount = cancelledCount;
        this.totalAppointmentsToday = totalAppointmentsToday;

    }
}