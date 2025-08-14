package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto;

import lombok.Data;
@Data
public class BranchDetailDto {
    private Long id;
    private String name;
    private String location;
    private long patientCount;
    private long doctorCount;
    private long appointmentsTodayCount;
}