package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.controller;


import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.AppointmentResponseDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.UserProfileDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.AppointmentService;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.OnlineUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class OnlineUserApiController {

    private final OnlineUserService onlineUserService;
    private final AppointmentService appointmentService;

    @GetMapping("/my-profile")
    public ResponseEntity<UserProfileDto> getMyProfile() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserProfileDto profile = onlineUserService.findProfileByUsername(username);
        return ResponseEntity.ok(profile);
    }

    @GetMapping("/my-appointments")
    public ResponseEntity<List<AppointmentResponseDto>> getMyAppointments() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<AppointmentResponseDto> appointments = appointmentService.findAppointmentsByUsername(username);
        return ResponseEntity.ok(appointments);
    }
}