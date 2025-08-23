package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.AdminUpdateRequestDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.StaffCreationRequestDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.StaffProfileDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface SuperAdminService {
    Page<StaffProfileDto> searchSuperAdmins(String keyword, Pageable pageable);
    void addSuperAdmin(StaffCreationRequestDto dto);

    void updateSuperAdmin(Integer userId, AdminUpdateRequestDto dto);
    void deleteSuperAdmin(Integer userId, String currentUsername);

    void updateUserStatus(Integer userId, boolean isActive, String currentUsername);

    StaffProfileDto findSuperAdminById(Integer userId);



    public void updateProfilePicture(String username, MultipartFile file) ;


    }