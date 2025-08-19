package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.StaffCreationRequestDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.StaffProfileDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BranchAdminService {


    Page<StaffProfileDto> getAllBranchAdmins(Pageable pageable);
    void addBranchAdmin(StaffCreationRequestDto dto);
    void updateBranchAdmin(Integer userId, StaffProfileDto dto);
    void deleteBranchAdmin(Integer userId);
}
