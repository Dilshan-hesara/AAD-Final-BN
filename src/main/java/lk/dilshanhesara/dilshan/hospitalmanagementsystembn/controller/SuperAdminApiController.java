package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.controller;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.*;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/super-admin")
@RequiredArgsConstructor
public class SuperAdminApiController {

    private final SuperAdminDashboardService dashboardService;
    private final PatientService patientService;
    private final DoctorService doctorService;
    private final AppointmentService appointmentService;
    private final ReceptionistService receptionistService;

    @GetMapping("/dashboard-summary")
    public ResponseEntity<SuperAdminDashboardDto> getSummary() {
        return ResponseEntity.ok(dashboardService.getDashboardSummary());
    }


    @GetMapping("/all-patients")
    public ResponseEntity<Page<PatientDto>> getAllPatients(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long branchId,
            Pageable pageable) {
        Page<PatientDto> patients = patientService.searchAllPatients(keyword, branchId, pageable);
        return ResponseEntity.ok(patients);
    }



    @GetMapping("/patients/{id}")
    public ResponseEntity<PatientDto> getPatientById(@PathVariable Long id) {
        PatientDto patient = patientService.findPatientById(id);
        return ResponseEntity.ok(patient);
    }


    // --- ADD THESE NEW ENDPOINTS ---
    @GetMapping("/all-doctors")
    public ResponseEntity<Page<DoctorDto>> getAllDoctors(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long branchId,
            Pageable pageable) {
        Page<DoctorDto> doctors = doctorService.searchAllDoctors(keyword, branchId, pageable);
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/doctors/{id}")
    public ResponseEntity<DoctorDto> getDoctorById(@PathVariable Integer id) {
        // You will need to create a findDoctorById service method
        DoctorDto doctor = doctorService.findDoctorById(id);
        return ResponseEntity.ok(doctor);
    }



    @GetMapping("/all-appointments")
    public ResponseEntity<Page<AppointmentResponseDto>> getAllAppointments(
            @RequestParam(required = false) String patientKeyword,
            @RequestParam(required = false) String doctorKeyword,
            @RequestParam(required = false) Long branchId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Pageable pageable) {

        Page<AppointmentResponseDto> appointments = appointmentService.searchAllAppointments(
                patientKeyword, doctorKeyword, branchId, status, date, pageable
        );
        return ResponseEntity.ok(appointments);
    }


    @GetMapping("/all-appointments/{id}")
    public ResponseEntity<AppointmentResponseDto> getAppointmentById(@PathVariable Long id) {
        AppointmentResponseDto appointment = appointmentService.findAppointmentById(id);
        return ResponseEntity.ok(appointment);
    }



    private final BranchAdminService branchAdminService;
    // ... your other services

    @GetMapping("/branch-admins")
    public ResponseEntity<Page<StaffProfileDto>> getAllBranchAdmins(Pageable pageable) {
        return ResponseEntity.ok(branchAdminService.getAllBranchAdmins(pageable));
    }

    @PostMapping("/branch-admins")
    public ResponseEntity<Void> addBranchAdmin(@RequestBody StaffCreationRequestDto dto) {
        branchAdminService.addBranchAdmin(dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/branch-admins/{id}")
    public ResponseEntity<Void> updateBranchAdmin(@PathVariable Integer id, @RequestBody AdminUpdateRequestDto dto) {
        branchAdminService.updateBranchAdmin(id, dto);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/branch-admins/{id}")
    public ResponseEntity<Void> deleteBranchAdmin(@PathVariable Integer id) {
        branchAdminService.deleteBranchAdmin(id);
        return ResponseEntity.ok().build();
    }


    // --- ADD THESE NEW ENDPOINTS ---
    @PatchMapping("/branch-admins/{id}/activate")
    public ResponseEntity<Void> activateBranchAdmin(@PathVariable Integer id) {
        branchAdminService.updateUserStatus(id, true);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/branch-admins/{id}/deactivate")
    public ResponseEntity<Void> deactivateBranchAdmin(@PathVariable Integer id) {
        branchAdminService.updateUserStatus(id, false);
        return ResponseEntity.ok().build();
    }


    // --- ADD THIS NEW ENDPOINT ---
    @GetMapping("/all-receptionists")
    public ResponseEntity<Page<StaffProfileDto>> getAllReceptionists(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long branchId,
            Pageable pageable) {
        Page<StaffProfileDto> receptionists = receptionistService.searchAllReceptionists(keyword, branchId, pageable);
        return ResponseEntity.ok(receptionists);
    }


    @GetMapping("/all-receptionists/{id}")
    public ResponseEntity<StaffProfileDto> getReceptionistById(@PathVariable Integer id) {
        // You'll need a findById method in your service
        StaffProfileDto receptionist = receptionistService.findReceptionistById(id);
        return ResponseEntity.ok(receptionist);
    }

    @PatchMapping("/all-receptionists/{id}/activate")
    public ResponseEntity<Void> activateReceptionist(@PathVariable Integer id) {
        // This reuses the service method we already created
        receptionistService.updateUserStatus(id, true);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/all-receptionists/{id}/deactivate")
    public ResponseEntity<Void> deactivateReceptionist(@PathVariable Integer id) {
        receptionistService.updateUserStatus(id, false);
        return ResponseEntity.ok().build();
    }
}