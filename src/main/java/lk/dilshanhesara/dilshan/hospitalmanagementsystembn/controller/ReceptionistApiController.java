package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.controller;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.StaffCreationRequestDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.StaffProfileDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.ReceptionistService;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.StaffProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/receptionists")
@RequiredArgsConstructor
public class ReceptionistApiController {

    private final ReceptionistService receptionistService;
    private final StaffProfileService staffProfileService;

    // SINGLE GET method for searching and pagination
    @GetMapping
    public ResponseEntity<Page<StaffProfileDto>> searchReceptionists(
            @RequestParam(required = false) String name,
            Pageable pageable) {
        StaffProfileDto adminProfile = staffProfileService.getCurrentLoggedInStaffProfile();
        Page<StaffProfileDto> receptionists = receptionistService.searchReceptionists(adminProfile.getBranchId(), name, pageable);
        return ResponseEntity.ok(receptionists);
    }

    @PostMapping
    public ResponseEntity<Void> addReceptionist(@RequestBody StaffCreationRequestDto dto) {
        StaffProfileDto adminProfile = staffProfileService.getCurrentLoggedInStaffProfile();
        dto.setBranchId(adminProfile.getBranchId());
        receptionistService.addReceptionist(dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateReceptionist(@PathVariable Integer id, @RequestBody StaffProfileDto dto) {
        receptionistService.updateReceptionist(id, dto);
        return ResponseEntity.ok().build();
    }

    // PATCH mappings for activate/deactivate
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activateReceptionist(@PathVariable Integer id) {
        receptionistService.updateUserStatus(id, true);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateReceptionist(@PathVariable Integer id) {
        receptionistService.updateUserStatus(id, false);
        return ResponseEntity.ok().build();
    }
}