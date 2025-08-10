package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service;


import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.PasswordChangeDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.StaffProfileDto;

public interface StaffProfileService {
    StaffProfileDto findStaffByUsername(String username);



    // --- ADD THIS NEW METHOD ---
    StaffProfileDto getCurrentLoggedInStaffProfile();




    public void updateProfile(String username, StaffProfileDto profileDto);

    public void changePassword(String username, PasswordChangeDto passwordDto) ;



}