package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.controller;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.StaffCreationRequestDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.StaffProfileDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.ReceptionistService;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.StaffProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;


import java.util.Map;



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

//    @PostMapping
//    public ResponseEntity<?> addReceptionist(@RequestBody StaffCreationRequestDto dto) {
//        try {
//            StaffProfileDto adminProfile = staffProfileService.getCurrentLoggedInStaffProfile();
//            // Ensure the logged-in admin has a valid profile and branch
//            if (adminProfile == null || adminProfile.getBranchId() == null) {
//                return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                        .body(Map.of("message", "Could not identify your branch. Please re-login."));
//            }
//            dto.setBranchId(adminProfile.getBranchId());
//            receptionistService.addReceptionist(dto);
//            return ResponseEntity.ok().build();
//
//        } catch (DataIntegrityViolationException e) {
//            // This error happens when a unique field (like username or email) already exists.
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .body(Map.of("message", "This username or email is already in use."));
//        } catch (Exception e) {
//            // This catches all other unexpected errors.
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Map.of("message", "An unexpected server error occurred."));
//        }
//    }

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