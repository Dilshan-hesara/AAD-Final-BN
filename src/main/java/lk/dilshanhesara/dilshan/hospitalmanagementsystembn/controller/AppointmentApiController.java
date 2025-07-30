package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.controller;


import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.AppointmentRequestDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.AppointmentResponseDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.StaffProfileDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.UpdateStatusRequestDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.AppointmentService;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.StaffProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentApiController {

    private final AppointmentService appointmentService;
    private final StaffProfileService staffProfileService;

    @GetMapping
    public ResponseEntity<List<AppointmentResponseDto>> getAppointmentsForBranch() {
        StaffProfileDto adminProfile = staffProfileService.getCurrentLoggedInStaffProfile();
        List<AppointmentResponseDto> appointments = appointmentService.findAppointmentsByBranch(adminProfile.getBranchId());
        return ResponseEntity.ok(appointments);
    }

    @PostMapping
    public ResponseEntity<Void> createAppointment(@RequestBody AppointmentRequestDto dto) {
        StaffProfileDto adminProfile = staffProfileService.getCurrentLoggedInStaffProfile();
        appointmentService.createAppointment(dto, adminProfile.getBranchId());
        return ResponseEntity.ok().build();
    }



    // This PATCH method for updating the status is also correct
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long id,
            @RequestBody UpdateStatusRequestDto requestDto) {
        appointmentService.updateAppointmentStatus(id, requestDto.getStatus());
        return ResponseEntity.ok().build();
    }
}