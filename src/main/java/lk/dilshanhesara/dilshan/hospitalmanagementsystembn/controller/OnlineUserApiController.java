package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.controller;


import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.*;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Appointment;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.*;
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



//
//    @PostMapping("/book-appointment")
//    public ResponseEntity<?> bookAppointment(@RequestBody AppointmentRequestDto dto) {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        Appointment newAppointment = appointmentService.createAppointmentForOnlineUser(dto, username);
//        return ResponseEntity.ok(Map.of("appointmentId", newAppointment.getId()));
//    }

    private final BranchService branchService;
    private final DoctorService doctorService;
    @GetMapping
    public ResponseEntity<List<BranchDto>> getAllBranches() {
        return ResponseEntity.ok(branchService.getAllBranches());
    }

    @GetMapping("/by-branch/{branchId}")
    public ResponseEntity<List<DoctorDto>> getDoctorsByBranch(@PathVariable Long branchId) {
        return ResponseEntity.ok(doctorService.findActiveDoctorsByBranch(branchId));
    }





    @GetMapping("/my-appointments")
    public ResponseEntity<List<AppointmentResponseDto>> getMyAppointments() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<AppointmentResponseDto> appointments = appointmentService.findAppointmentsByUsername(username);
        return ResponseEntity.ok(appointments);
    }


    @PostMapping("/book-appointment")
    public ResponseEntity<?> bookAppointment(@RequestBody OnlineUserAppointmentRequestDto dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Appointment newAppointment = appointmentService.createAppointmentForOnlineUser(dto, username);

        return ResponseEntity.ok(Map.of("appointmentId", newAppointment.getId()));
    }




}