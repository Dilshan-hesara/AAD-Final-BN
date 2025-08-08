package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service;


import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.RegistrationRequestDto;

public interface UserService {
    void registerOnlineUser(RegistrationRequestDto registrationDto);
}
