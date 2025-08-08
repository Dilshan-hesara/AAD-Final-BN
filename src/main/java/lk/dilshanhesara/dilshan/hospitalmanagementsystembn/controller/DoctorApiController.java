package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.controller;


import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.DoctorDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.StaffProfileDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.DoctorService;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.StaffProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorApiController {

    private final DoctorService doctorService;
    private final StaffProfileService staffProfileService; // To get the admin's branch

    @GetMapping
    public ResponseEntity<List<DoctorDto>> getDoctorsForCurrentBranch() {

        StaffProfileDto adminProfile = staffProfileService.findStaffByUsername(
                org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName()
        );
        List<DoctorDto> doctors = doctorService.findDoctorsByBranch(adminProfile.getBranchId());
        return ResponseEntity.ok(doctors);
    }

    @PostMapping
    public ResponseEntity<Void> addDoctor(@RequestBody DoctorDto doctorDto) {

        StaffProfileDto adminProfile = staffProfileService.findStaffByUsername(
                org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName()
        );
        doctorDto.setBranchId(adminProfile.getBranchId());

        doctorService.addDoctor(doctorDto);
        return ResponseEntity.ok().build();
    }

    // --- ADD THIS NEW ENDPOINT ---
    @GetMapping("/by-branch/{branchId}")
    public ResponseEntity<List<DoctorDto>> getDoctorsByBranch(@PathVariable Long branchId) {
        return ResponseEntity.ok(doctorService.findDoctorsByBranch(branchId));
    }
}