package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.controller;


import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.AppointmentRequestDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.AppointmentResponseDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.UserProfileDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Appointment;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.AppointmentService;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.OnlineUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

//    @GetMapping("/my-appointments")
//    public ResponseEntity<List<AppointmentResponseDto>> getMyAppointments() {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        List<AppointmentResponseDto> appointments = appointmentService.findAppointmentsByUsername(username);
//        return ResponseEntity.ok(appointments);
//    }



    @GetMapping("/my-appointments")
    public ResponseEntity<List<AppointmentResponseDto>> getMyAppointments() {
        // Get the username of the currently logged-in user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Fetch appointments for that user
        List<AppointmentResponseDto> appointments = appointmentService.findAppointmentsByUsername(username);

        return ResponseEntity.ok(appointments);
    }




    @PostMapping("/book-appointment")
    public ResponseEntity<?> bookAppointment(@RequestBody AppointmentRequestDto dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // This method now returns the created appointment
        Appointment newAppointment = appointmentService.createAppointmentForOnlineUser(dto, username);
        // Return the ID of the new appointment
        return ResponseEntity.ok(Map.of("appointmentId", newAppointment.getId()));
    }

    // --- ADD THIS NEW ENDPOINT TO CONFIRM PAYMENT ---
    @PostMapping("/confirm-payment")
    public ResponseEntity<Void> confirmPayment(@RequestBody Map<String, Long> payload) {
        Long appointmentId = payload.get("appointmentId");
        appointmentService.confirmAppointmentPayment(appointmentId);
        return ResponseEntity.ok().build();
    }
}