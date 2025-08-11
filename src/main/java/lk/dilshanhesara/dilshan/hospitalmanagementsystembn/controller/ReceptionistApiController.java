package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.controller;


import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.StaffCreationRequestDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.StaffProfileDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.ReceptionistService;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.StaffProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/receptionists")
@RequiredArgsConstructor
public class ReceptionistApiController {

    private final ReceptionistService receptionistService;
    private final StaffProfileService staffProfileService;

    @GetMapping
    public ResponseEntity<List<StaffProfileDto>> getReceptionistsForCurrentBranch() {
        StaffProfileDto adminProfile = staffProfileService.findStaffByUsername(
                org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName()
        );
        List<StaffProfileDto> receptionists = receptionistService.findReceptionistsByBranch(adminProfile.getBranchId());
        return ResponseEntity.ok(receptionists);
    }

    @PostMapping
    public ResponseEntity<Void> addReceptionist(@RequestBody StaffCreationRequestDto receptionistDto) {
        StaffProfileDto adminProfile = staffProfileService.findStaffByUsername(
                org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName()
        );
        receptionistDto.setBranchId(adminProfile.getBranchId()); // Auto-assign branch
        receptionistService.addReceptionist(receptionistDto);
        return ResponseEntity.ok().build();
    }


    // --- ADD THESE NEW ENDPOINTS ---
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