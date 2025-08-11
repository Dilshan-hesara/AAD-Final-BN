package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.StaffCreationRequestDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.StaffProfileDto;

import java.util.List;

public interface ReceptionistService {


    List<StaffProfileDto> findReceptionistsByBranch(Long branchId);
    void addReceptionist(StaffCreationRequestDto receptionistDto);

    public void updateUserStatus(Integer userId, boolean isActive) ;
}
