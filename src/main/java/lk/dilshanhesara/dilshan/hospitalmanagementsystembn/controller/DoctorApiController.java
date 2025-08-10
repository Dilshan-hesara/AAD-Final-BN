package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.controller;


import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.DoctorDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.StaffProfileDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.DoctorService;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.StaffProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorApiController {

    private final DoctorService doctorService;
    private final StaffProfileService staffProfileService; // To get the admin's branch


    // --- MAPPING 1: GET /api/doctors (Handles searching and pagination) ---
    // This is the ONLY method that handles the base GET request.
    @GetMapping
    public ResponseEntity<Page<DoctorDto>> searchDoctors(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String specialization,
            Pageable pageable) {

        StaffProfileDto adminProfile = staffProfileService.getCurrentLoggedInStaffProfile();
        Page<DoctorDto> results = doctorService.searchDoctors(adminProfile.getBranchId(), name, specialization, pageable);
        return ResponseEntity.ok(results);
    }

    // --- MAPPING 2: GET /api/doctors/by-branch/{branchId} (For dropdowns) ---
    @GetMapping("/by-branch/{branchId}")
    public ResponseEntity<List<DoctorDto>> getDoctorsByBranch(@PathVariable Long branchId) {
        List<DoctorDto> doctors = doctorService.findActiveDoctorsByBranch(branchId);
        return ResponseEntity.ok(doctors);
    }

    // --- MAPPING 3: GET /api/doctors/inactive (For the inactive doctors table) ---
    @GetMapping("/inactive")
    public ResponseEntity<List<DoctorDto>> getInactiveDoctorsForCurrentBranch() {
        StaffProfileDto adminProfile = staffProfileService.getCurrentLoggedInStaffProfile();
        List<DoctorDto> doctors = doctorService.findInactiveDoctorsByBranch(adminProfile.getBranchId());
        return ResponseEntity.ok(doctors);
    }

    // --- MAPPING 4: POST /api/doctors (To create a new doctor) ---
    @PostMapping
    public ResponseEntity<Void> addDoctor(@RequestBody DoctorDto doctorDto) {
        StaffProfileDto adminProfile = staffProfileService.getCurrentLoggedInStaffProfile();
        doctorDto.setBranchId(adminProfile.getBranchId());
        doctorService.addDoctor(doctorDto);
        return ResponseEntity.ok().build();
    }

    // --- MAPPING 5: PUT /api/doctors/{id} (To update a doctor's details) ---
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateDoctor(@PathVariable Integer id, @RequestBody DoctorDto doctorDto) {
        doctorService.updateDoctor(id, doctorDto);
        return ResponseEntity.ok().build();
    }

    // --- MAPPING 6: PATCH /api/doctors/{id}/deactivate ---
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateDoctor(@PathVariable int id) {
        doctorService.deactivateDoctor(id);
        return ResponseEntity.ok().build();
    }

    // --- MAPPING 7: PATCH /api/doctors/{id}/activate ---
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activateDoctor(@PathVariable int id) {
        doctorService.activateDoctor(id);
        return ResponseEntity.ok().build();
    }



    // --- THIS IS THE ENDPOINT FOR "DOCTORS ON DUTY" ---
    @GetMapping("/active-list")
    public ResponseEntity<List<DoctorDto>> getActiveDoctorListForDashboard() {
        StaffProfileDto adminProfile = staffProfileService.getCurrentLoggedInStaffProfile();
        List<DoctorDto> activeDoctors = doctorService.findActiveDoctorsByBranch(adminProfile.getBranchId());
        return ResponseEntity.ok(activeDoctors);
    }
//    // This is now the ONLY method that handles GET /api/doctors
//    @GetMapping
//    public ResponseEntity<List<DoctorDto>> getActiveDoctorsForCurrentBranch() {
//        StaffProfileDto adminProfile = staffProfileService.getCurrentLoggedInStaffProfile();
//        List<DoctorDto> doctors = doctorService.findActiveDoctorsByBranch(adminProfile.getBranchId());
//        return ResponseEntity.ok(doctors);
//    }
//
//    // Your endpoint to get doctors by branch ID (for dropdowns)
//    @GetMapping("/by-branch/{branchId}")
//    public ResponseEntity<List<DoctorDto>> getDoctorsByBranch(@PathVariable Long branchId) {
//        // You can reuse the service method or create a new one
//        List<DoctorDto> doctors = doctorService.findActiveDoctorsByBranch(branchId);
//        return ResponseEntity.ok(doctors);
//    }
//
//    // Your POST method for creating a doctor
//    @PostMapping
//    public ResponseEntity<Void> addDoctor(@RequestBody DoctorDto doctorDto) {
//        StaffProfileDto adminProfile = staffProfileService.getCurrentLoggedInStaffProfile();
//        doctorDto.setBranchId(adminProfile.getBranchId());
//        doctorService.addDoctor(doctorDto);
//        return ResponseEntity.ok().build();
//    }
//
//    @PostMapping
//    public ResponseEntity<Void> addDoctor(@RequestBody DoctorDto doctorDto) {
//
//        StaffProfileDto adminProfile = staffProfileService.findStaffByUsername(
//                org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName()
//        );
//        doctorDto.setBranchId(adminProfile.getBranchId());
//
//        doctorService.addDoctor(doctorDto);
//        return ResponseEntity.ok().build();
//    }
//
//    // --- ADD THIS NEW ENDPOINT ---
//    @GetMapping("/by-branch/{branchId}")
//    public ResponseEntity<List<DoctorDto>> getDoctorsByBranch(@PathVariable Long branchId) {
//        return ResponseEntity.ok(doctorService.findDoctorsByBranch(branchId));
//    }
//
//    @GetMapping
//    public ResponseEntity<List<DoctorDto>> getActiveDoctorsForCurrentBranch() {
//        StaffProfileDto adminProfile = staffProfileService.getCurrentLoggedInStaffProfile();
//        List<DoctorDto> doctors = doctorService.findActiveDoctorsByBranch(adminProfile.getBranchId());
//        return ResponseEntity.ok(doctors);
//    }
// This is now the ONLY method that handles GET /api/doctors

    // Your endpoint to get doctors by branch ID (for dropdowns)
//    @GetMapping("/by-branch/{branchId}")
//    public ResponseEntity<List<DoctorDto>> getDoctorsByBranch(@PathVariable Long branchId) {
//        // You can reuse the service method or create a new one
//        List<DoctorDto> doctors = doctorService.findActiveDoctorsByBranch(branchId);
//        return ResponseEntity.ok(doctors);
//    }

//    // Your POST method for creating a doctor
//    @PostMapping
//    public ResponseEntity<Void> addDoctor(@RequestBody DoctorDto doctorDto) {
//        StaffProfileDto adminProfile = staffProfileService.getCurrentLoggedInStaffProfile();
//        doctorDto.setBranchId(adminProfile.getBranchId());
//        doctorService.addDoctor(doctorDto);
//        return ResponseEntity.ok().build();
//    }
//
//    @GetMapping
//    public ResponseEntity<Page<DoctorDto>> searchDoctors(
//            @RequestParam(required = false) String name,
//            @RequestParam(required = false) String specialization,
//            Pageable pageable) {
//
//        StaffProfileDto adminProfile = staffProfileService.getCurrentLoggedInStaffProfile();
//        Page<DoctorDto> results = doctorService.searchDoctors(adminProfile.getBranchId(), name, specialization, pageable);
//        return ResponseEntity.ok(results);
//    }


//    @PatchMapping("/{id}/deactivate")
//    public ResponseEntity<Void> deactivateDoctor(@PathVariable int id) {
//        doctorService.deactivateDoctor(id);
//        return ResponseEntity.ok().build();
//    }
//
//    @PatchMapping("/{id}/activate")
//    public ResponseEntity<Void> activateDoctor(@PathVariable int id) {
//        doctorService.activateDoctor(id);
//        return ResponseEntity.ok().build();
//    }
//
//    // --- ADD THIS NEW ENDPOINT ---
//    @GetMapping("/inactive")
//    public ResponseEntity<List<DoctorDto>> getInactiveDoctorsForCurrentBranch() {
//        StaffProfileDto adminProfile = staffProfileService.getCurrentLoggedInStaffProfile();
//        List<DoctorDto> doctors = doctorService.findInactiveDoctorsByBranch(adminProfile.getBranchId());
//        return ResponseEntity.ok(doctors);
//    }
//
//
//
//
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Void> updateDoctor(@PathVariable Integer id, @RequestBody DoctorDto doctorDto) {
//        doctorService.updateDoctor(id, doctorDto);
//        return ResponseEntity.ok().build();
//    }





}