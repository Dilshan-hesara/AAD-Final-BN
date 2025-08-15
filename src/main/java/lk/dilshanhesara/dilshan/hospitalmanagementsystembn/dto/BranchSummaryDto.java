package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto;

import lombok.Data;

@Data
public class BranchSummaryDto extends BranchDto {
    private long totalPatientCount;
    private long activeDoctorCount;
    private long totalAppointmentCount;
    private long todaysAppointmentCount;
    private long activeReceptionistCount;

}