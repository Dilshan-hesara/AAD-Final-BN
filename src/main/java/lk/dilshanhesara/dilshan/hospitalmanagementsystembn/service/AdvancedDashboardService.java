package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.AdvancedBranchDashboardDto;

import java.time.LocalDateTime;

public interface AdvancedDashboardService {

    public AdvancedBranchDashboardDto getAdvancedDashboardSummary(Long branchId, LocalDateTime startDate, LocalDateTime endDate) ;

    }
