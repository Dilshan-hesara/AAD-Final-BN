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
    private final StaffProfileService staffProfileService;


    @GetMapping
    public ResponseEntity<Page<DoctorDto>> searchDoctors(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String specialization,
            Pageable pageable) {

        StaffProfileDto adminProfile = staffProfileService.getCurrentLoggedInStaffProfile();
        Page<DoctorDto> results = doctorService.searchDoctors(adminProfile.getBranchId(), name, specialization, pageable);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/by-branch/{branchId}")
    public ResponseEntity<List<DoctorDto>> getDoctorsByBranch(@PathVariable Long branchId) {
        List<DoctorDto> doctors = doctorService.findActiveDoctorsByBranch(branchId);
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<DoctorDto>> getInactiveDoctorsForCurrentBranch() {
        StaffProfileDto adminProfile = staffProfileService.getCurrentLoggedInStaffProfile();
        List<DoctorDto> doctors = doctorService.findInactiveDoctorsByBranch(adminProfile.getBranchId());
        return ResponseEntity.ok(doctors);
    }

    @PostMapping
    public ResponseEntity<Void> addDoctor(@RequestBody DoctorDto doctorDto) {
        StaffProfileDto adminProfile = staffProfileService.getCurrentLoggedInStaffProfile();
        doctorDto.setBranchId(adminProfile.getBranchId());
        doctorService.addDoctor(doctorDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateDoctor(@PathVariable Integer id, @RequestBody DoctorDto doctorDto) {
        doctorService.updateDoctor(id, doctorDto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateDoctor(@PathVariable int id) {
        doctorService.deactivateDoctor(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activateDoctor(@PathVariable int id) {
        doctorService.activateDoctor(id);
        return ResponseEntity.ok().build();
    }



    @GetMapping("/active-list")
    public ResponseEntity<List<DoctorDto>> getActiveDoctorListForDashboard() {
        StaffProfileDto adminProfile = staffProfileService.getCurrentLoggedInStaffProfile();
        List<DoctorDto> activeDoctors = doctorService.findActiveDoctorsByBranch(adminProfile.getBranchId());
        return ResponseEntity.ok(activeDoctors);
    }






    @GetMapping("/search")
    public ResponseEntity<Page<DoctorDto>> searchDoctors(
            @RequestParam(defaultValue = "") String name,
            Pageable pageable) {
        StaffProfileDto adminProfile = staffProfileService.getCurrentLoggedInStaffProfile();
        return ResponseEntity.ok(doctorService.searchActiveDoctorsByNameAndBranch(name, adminProfile.getBranchId(), pageable));
    }



}