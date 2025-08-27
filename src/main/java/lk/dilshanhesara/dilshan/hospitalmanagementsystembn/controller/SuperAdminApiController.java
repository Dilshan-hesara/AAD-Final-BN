package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.controller;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.*;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.UserAccount;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.UserAccountRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Map;

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

//    @GetMapping("/branch-admins")
//    public ResponseEntity<Page<StaffProfileDto>> getAllBranchAdmins(Pageable pageable) {
//        return ResponseEntity.ok(branchAdminService.getAllBranchAdmins(pageable));
//    }

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


//    @DeleteMapping("/branch-admins/{id}")
//    public ResponseEntity<Void> deleteBranchAdmin(@PathVariable Integer id) {
//        branchAdminService.deleteBranchAdmin(id);
//        return ResponseEntity.ok().build();
//    }


    // --- ADD THESE NEW ENDPOINTS ---
//    @PatchMapping("/branch-admins/{id}/activate")
//    public ResponseEntity<Void> activateBranchAdmin(@PathVariable Integer id) {
//        branchAdminService.updateUserStatus(id, true);
//        return ResponseEntity.ok().build();
//    }
//
//    @PatchMapping("/branch-admins/{id}/deactivate")
//    public ResponseEntity<Void> deactivateBranchAdmin(@PathVariable Integer id) {
//        branchAdminService.updateUserStatus(id, false);
//        return ResponseEntity.ok().build();
//    }


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



    @PostMapping("/all-receptionists")
    public ResponseEntity<Void> addReceptionist(@RequestBody StaffCreationRequestDto dto) {
        receptionistService.addReceptionist(dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/all-receptionists/{id}")
    public ResponseEntity<Void> updateReceptionist(@PathVariable Integer id, @RequestBody AdminUpdateRequestDto dto) {
        receptionistService.updateReceptionist(id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/all-receptionists/{id}")
    public ResponseEntity<Void> deleteReceptionist(@PathVariable Integer id) {
        receptionistService.deleteReceptionist(id);
        return ResponseEntity.ok().build();
    }




    private final SuperAdminService superAdminService;





    @GetMapping("/manage-admins")
    public ResponseEntity<Page<StaffProfileDto>> searchSuperAdmins(@RequestParam(required=false) String keyword, Pageable pageable) {
        return ResponseEntity.ok(superAdminService.searchSuperAdmins(keyword, pageable));
    }
    @GetMapping("/manage-admins/{id}")
    public ResponseEntity<StaffProfileDto> getSuperAdminById(@PathVariable Integer id) {
        return ResponseEntity.ok(superAdminService.findSuperAdminById(id));
    }
    @PostMapping("/manage-admins")
    public ResponseEntity<Void> addSuperAdmin(@RequestBody StaffCreationRequestDto dto) {
        superAdminService.addSuperAdmin(dto);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/manage-admins/{id}")
    public ResponseEntity<Void> updateSuperAdmin(@PathVariable Integer id, @RequestBody AdminUpdateRequestDto dto) {
        superAdminService.updateSuperAdmin(id, dto);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/manage-admins/{id}")
    public ResponseEntity<Void> deleteSuperAdmin(@PathVariable Integer id) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        superAdminService.deleteSuperAdmin(id, currentUsername);
        return ResponseEntity.ok().build();
    }
    @PatchMapping("/manage-admins/{id}/status")
    public ResponseEntity<Void> updateSuperAdminStatus(@PathVariable Integer id, @RequestBody Map<String, Boolean> payload) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        superAdminService.updateUserStatus(id, payload.get("isActive"), currentUsername);
        return ResponseEntity.ok().build();
    }















    private final UserAccountRepository userAccountRepository;
//
//    @GetMapping("/my-profile")
//    public ResponseEntity<StaffProfileDto> getMyProfile() {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        return ResponseEntity.ok(superAdminService.findSuperAdminByUsername(username)); // You'll need this service method
//    }
//    @PostMapping("/my-profile/update")
//    public ResponseEntity<Void> updateMyProfile(@RequestBody AdminUpdateRequestDto dto) {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//
//        // --- 2. FIND THE USER'S ID FROM THE USERNAME ---
//        UserAccount account = userAccountRepository.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        Integer userId = account.getUserId();
//
//        // --- 3. CALL THE SERVICE METHOD CORRECTLY ---
//        superAdminService.updateSuperAdmin(userId, dto);
//
//        return ResponseEntity.ok().build();
//    }
//
//    @PostMapping("/my-profile/upload-picture")
//    public ResponseEntity<Void> uploadProfilePicture(@RequestParam("file") MultipartFile file) {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        superAdminService.updateProfilePicture(username, file);
//        return ResponseEntity.ok().build();
//    }



    @GetMapping("/my-profile")
    public ResponseEntity<StaffProfileDto> getMyProfile() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(superAdminService.findSuperAdminByUsername(username));
    }

    @PostMapping("/my-profile/update")
    public ResponseEntity<Void> updateMyProfile(@RequestBody AdminUpdateRequestDto dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        superAdminService.updateMyProfile(username, dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/my-profile/upload-picture")
    public ResponseEntity<Void> uploadProfilePicture(@RequestParam("file") MultipartFile file) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        superAdminService.updateProfilePicture(username, file);
        return ResponseEntity.ok().build();
    }





















    @GetMapping("/branch-admins")
    public ResponseEntity<Page<StaffProfileDto>> searchBranchAdmins(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long branchId, // Add this parameter
            Pageable pageable) {
        // Pass all parameters to the service
        return ResponseEntity.ok(branchAdminService.searchBranchAdmins(keyword, branchId, pageable));
    }

    @GetMapping("/branch-admins/{id}")
    public ResponseEntity<StaffProfileDto> getBranchAdminById(@PathVariable Integer id) {
        return ResponseEntity.ok(branchAdminService.findBranchAdminById(id));
    }

//fefv


//    @GetMapping("/branch-admins")
//    public ResponseEntity<Page<StaffProfileDto>> searchBranchAdmsins(
//            @RequestParam(required = false) String keyword,
//            @RequestParam(required = false) Long branchId,
//            Pageable pageable) {
//        return ResponseEntity.ok(branchAdminService.searchBranchAdmins(keyword, branchId, pageable));
//    }
//
//    @GetMapping("/branch-admins")
//    public ResponseEntity<Page<StaffProfileDto>> searchBranchAdmins(
//            @RequestParam(required = false) String keyword,
//            @RequestParam(required = false) Long branchId,
//            Pageable pageable) {
//        return ResponseEntity.ok(branchAdminService.searchBranchAdmins(keyword, branchId, pageable));
//    }


//
//    @PostMapping("/branch-admins")
//    public ResponseEntity<Void> addBranchAdmin(@RequestBody StaffCreationRequestDto dto) {
//        branchAdminService.addBranchAdmin(dto);
//        return ResponseEntity.ok().build();
//    }
//
//    @PutMapping("/branch-admins/{id}")
//    public ResponseEntity<Void> updateBranchAdmin(@PathVariable Integer id, @RequestBody AdminUpdateRequestDto dto) {
//        branchAdminService.updateBranchAdmin(id, dto);
//        return ResponseEntity.ok().build();
//    }

    @DeleteMapping("/branch-admins/{id}")
    public ResponseEntity<Void> deleteBranchAdmin(@PathVariable Integer id) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        branchAdminService.deleteBranchAdmin(id, currentUsername);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/branch-admins/{id}/status")
    public ResponseEntity<Void> updateBranchAdminStatus(@PathVariable Integer id, @RequestBody Map<String, Boolean> payload) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        branchAdminService.updateUserStatus(id, payload.get("isActive"), currentUsername);
        return ResponseEntity.ok().build();
    }


}
