package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.AdminUpdateRequestDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.StaffCreationRequestDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.StaffProfileDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BranchAdminService {

//
//    Page<StaffProfileDto> getAllBranchAdmins(Pageable pageable);
//    void addBranchAdmin(StaffCreationRequestDto dto);
//    public void updateBranchAdmin(Integer userId, AdminUpdateRequestDto dto) ;
//    void deleteBranchAdmin(Integer userId);
//
//    public void updateUserStatus(Integer userId, boolean isActive) ;
//
//
//public Page<StaffProfileDto> searchBranchAdmins(String keyword, Long branchId, Pageable pageable) ;
Page<StaffProfileDto> searchBranchAdmins(String keyword, Long branchId, Pageable pageable);
    StaffProfileDto findBranchAdminById(Integer userId);
    void addBranchAdmin(StaffCreationRequestDto dto);
    void updateBranchAdmin(Integer userId, AdminUpdateRequestDto dto);
    void deleteBranchAdmin(Integer userId, String currentUsername);
    void updateUserStatus(Integer userId, boolean isActive, String currentUsername);
    }
