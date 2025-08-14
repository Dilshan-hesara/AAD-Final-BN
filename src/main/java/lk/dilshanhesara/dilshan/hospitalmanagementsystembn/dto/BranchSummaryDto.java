package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto;

import lombok.Data;

@Data
public class BranchSummaryDto extends BranchDto {
    private long patientCount;
    private long doctorCount;
}