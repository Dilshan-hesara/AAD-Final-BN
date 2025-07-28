package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.controller;


import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.StaffProfileDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.StaffProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}