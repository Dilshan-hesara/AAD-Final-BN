package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.UserProfileDto;

public interface OnlineUserService {
    UserProfileDto findProfileByUsername(String username);
}