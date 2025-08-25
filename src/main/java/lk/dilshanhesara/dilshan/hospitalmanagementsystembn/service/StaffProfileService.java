package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service;


import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.PasswordChangeDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.StaffProfileDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.UserAccount;

public interface StaffProfileService {
    StaffProfileDto findStaffByUsername(String username);


    StaffProfileDto getCurrentLoggedInStaffProfile();

    public void updateProfile(String username, StaffProfileDto profileDto);

    public void changePassword(String username, PasswordChangeDto passwordDto) ;

     StaffProfileDto convertToStaffProfileDto(UserAccount account) ;


}