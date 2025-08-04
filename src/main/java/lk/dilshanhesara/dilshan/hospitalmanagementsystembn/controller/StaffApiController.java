package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.controller;


import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.PasswordChangeDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.StaffProfileDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.StaffProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/staff")
public class StaffApiController {

    @Autowired
    private StaffProfileService staffProfileService;

    @GetMapping("/my-profile")
    public ResponseEntity<StaffProfileDto> getMyProfile() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();


        StaffProfileDto profile = staffProfileService.findStaffByUsername(username);

        return ResponseEntity.ok(profile);
    }







    @PostMapping("/my-profile/update")
    public ResponseEntity<Void> updateMyProfile(@RequestBody StaffProfileDto profileDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        staffProfileService.updateProfile(username, profileDto);
        return ResponseEntity.ok().build();
    }


}