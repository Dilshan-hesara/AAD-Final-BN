package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.AdminUpdateRequestDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.StaffCreationRequestDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.StaffProfileDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BranchAdminService {


    Page<StaffProfileDto> getAllBranchAdmins(Pageable pageable);
    void addBranchAdmin(StaffCreationRequestDto dto);
    public void updateBranchAdmin(Integer userId, AdminUpdateRequestDto dto) ;
    void deleteBranchAdmin(Integer userId);
}
