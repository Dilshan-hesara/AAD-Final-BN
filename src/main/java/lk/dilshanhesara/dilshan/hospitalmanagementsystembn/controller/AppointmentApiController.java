package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.controller;


import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.AppointmentRequestDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.AppointmentResponseDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.StaffProfileDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.UpdateStatusRequestDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Appointment;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.AppointmentService;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.StaffProfileService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentApiController {

    private final AppointmentService appointmentService;
    private final StaffProfileService staffProfileService;
    private final ModelMapper modelMapper;

//    @GetMapping
//    public ResponseEntity<List<AppointmentResponseDto>> getAppointmentsForBranch() {
//        StaffProfileDto adminProfile = staffProfileService.getCurrentLoggedInStaffProfile();
//        List<AppointmentResponseDto> appointments = appointmentService.findAppointmentsByBranch(adminProfile.getBranchId());
//        return ResponseEntity.ok(appointments);
//    }
//
//    @PostMapping
//    public ResponseEntity<Void> createAppointment(@RequestBody AppointmentRequestDto dto) {
//        StaffProfileDto adminProfile = staffProfileService.getCurrentLoggedInStaffProfile();
//        appointmentService.createAppointment(dto, adminProfile.getBranchId());
//        return ResponseEntity.ok().build();
//    }
//
//
//
//    // This PATCH method for updating the status is also correct
//    @PatchMapping("/{id}/status")
//    public ResponseEntity<Void> updateStatus(
//            @PathVariable Long id,
//            @RequestBody UpdateStatusRequestDto requestDto) {
//        appointmentService.updateAppointmentStatus(id, requestDto.getStatus());
//        return ResponseEntity.ok().build();
//    }
//
//    // In AppointmentApiController.java
//    @GetMapping("/online-bookings/today")
//    public ResponseEntity<List<AppointmentResponseDto>> getTodaysOnlineBookings() {
//        StaffProfileDto adminProfile = staffProfileService.getCurrentLoggedInStaffProfile();
//        List<AppointmentResponseDto> appointments = appointmentService.findOnlineUserAppointmentsForToday(adminProfile.getBranchId());
//        return ResponseEntity.ok(appointments);
//    }
//
//
//
//
//
//
//
//
//
////    SERCH
//
//
//    @GetMapping
//    public ResponseEntity<Page<AppointmentResponseDto>> searchAppointments(
//            @RequestParam(required = false) String patientName,
//            @RequestParam(required = false) String status,
//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
//            Pageable pageable) { // Spring automatically handles page, size, sort
//
//        StaffProfileDto adminProfile = staffProfileService.getCurrentLoggedInStaffProfile();
//        Page<AppointmentResponseDto> results = appointmentService.searchAppointments(adminProfile.getBranchId(), patientName, status, date, pageable);
//        return ResponseEntity.ok(results);
//    }







    // This is the single, correct GET method that handles searching and pagination
    @GetMapping
    public ResponseEntity<Page<AppointmentResponseDto>> searchAppointments(
            @RequestParam(required = false) String patientName,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Pageable pageable) {

        StaffProfileDto adminProfile = staffProfileService.getCurrentLoggedInStaffProfile();
        Page<AppointmentResponseDto> results = appointmentService.searchAppointments(adminProfile.getBranchId(), patientName, status, date, pageable);

        return ResponseEntity.ok(results);
    }

    // Endpoint for online user's appointments
    @GetMapping("/online-bookings/today")
    public ResponseEntity<List<AppointmentResponseDto>> getTodaysOnlineBookings() {
        StaffProfileDto adminProfile = staffProfileService.getCurrentLoggedInStaffProfile();
        List<AppointmentResponseDto> appointments = appointmentService.findOnlineUserAppointmentsForToday(adminProfile.getBranchId());
        return ResponseEntity.ok(appointments);
    }

    // POST method for creating appointments
    @PostMapping
    public ResponseEntity<Void> createAppointment(@RequestBody AppointmentRequestDto dto) {
        StaffProfileDto adminProfile = staffProfileService.getCurrentLoggedInStaffProfile();
        appointmentService.createAppointment(dto, adminProfile.getBranchId());
        return ResponseEntity.ok().build();
    }

    // PATCH method for updating the status
    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long id,
            @RequestBody UpdateStatusRequestDto requestDto) {
        appointmentService.updateAppointmentStatus(id, requestDto.getStatus());
        return ResponseEntity.ok().build();
    }





    @PostMapping("/by-staff")
    public ResponseEntity<AppointmentResponseDto> createAppointmentByStaff(@RequestBody AppointmentRequestDto dto) {
        // Get the branch ID from the logged-in staff member
        StaffProfileDto staffProfile = staffProfileService.getCurrentLoggedInStaffProfile();

        Appointment newAppointment = appointmentService.createAppointmentByStaff(dto, staffProfile.getBranchId());

        // Return a DTO of the newly created appointment
        AppointmentResponseDto responseDto = modelMapper.map(newAppointment, AppointmentResponseDto.class);
        return ResponseEntity.ok(responseDto);

    }


    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDto> getAppointmentById(@PathVariable Long id) {
        AppointmentResponseDto appointment = appointmentService.findAppointmentById(id);
        return ResponseEntity.ok(appointment);
    }
}

