package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.OnlineUserRegistrationDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.PasswordChangeDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.UserProfileDto;
import org.springframework.web.multipart.MultipartFile;

public interface OnlineUserService {
    UserProfileDto findProfileByUsername(String username);
    void registerNewOnlineUser(OnlineUserRegistrationDto registrationDto);


    public void updateMyProfile(String username, UserProfileDto dto);
    public void changeMyPassword(String username, PasswordChangeDto dto);
}