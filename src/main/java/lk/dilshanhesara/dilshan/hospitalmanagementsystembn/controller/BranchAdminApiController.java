package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.controller;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.StaffProfileDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Notification;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.impl.NotificationService;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.StaffProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/branch-admin/notifications")
@RequiredArgsConstructor
//@PreAuthorize("hasAuthority('ROLE_BRANCH_ADMIN')")
public class BranchAdminApiController {

    private final NotificationService notificationService;
    private final StaffProfileService staffProfileService;

    @GetMapping("/unread")
    public ResponseEntity<List<Notification>> getUnread() {
        StaffProfileDto myProfile = staffProfileService.getCurrentLoggedInStaffProfile();
        return ResponseEntity.ok(notificationService.getUnreadNotifications(myProfile.getUsername()));
    }

    @PostMapping("/mark-as-read")
    public ResponseEntity<Void> markAsRead() {
        StaffProfileDto myProfile = staffProfileService.getCurrentLoggedInStaffProfile();
        notificationService.markNotificationsAsRead(myProfile.getUsername());
        return ResponseEntity.ok().build();
    }
}