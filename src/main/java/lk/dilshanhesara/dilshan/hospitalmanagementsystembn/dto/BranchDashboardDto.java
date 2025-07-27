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
}