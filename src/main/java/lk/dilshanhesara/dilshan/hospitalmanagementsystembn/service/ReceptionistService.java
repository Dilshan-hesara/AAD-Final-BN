package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.StaffCreationRequestDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.StaffProfileDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReceptionistService {


    List<StaffProfileDto> findReceptionistsByBranch(Long branchId);
    void addReceptionist(StaffCreationRequestDto receptionistDto);

    public void updateUserStatus(Integer userId, boolean isActive) ;



    public Page<StaffProfileDto> searchReceptionists(Long branchId, String name, Pageable pageable) ;
    public void updateReceptionist(Integer userId, StaffProfileDto dto);

     StaffProfileDto convertToStaffProfileDto(UserAccount account) ;


    public Page<StaffProfileDto> searchAllReceptionists(String keyword, Long branchId, Pageable pageable) ;


    }
