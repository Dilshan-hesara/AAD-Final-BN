package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.controller;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.ContactDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.MessageDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.MessageRequestDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.StaffProfileDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.UserAccount;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.MessageService;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.StaffProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageApiController {
    private final MessageService messageService;
    private final StaffProfileService staffProfileService;
//
//    @GetMapping("/conversation/{otherBranchId}")
//    public ResponseEntity<List<MessageDto>> getConversation(@PathVariable Long otherBranchId) {
//        StaffProfileDto myProfile = staffProfileService.getCurrentLoggedInStaffProfile();
//        return ResponseEntity.ok(messageService.getConversation(myProfile.getBranchId(), otherBranchId));
//    }
//
//    @PostMapping("/send")
//    public ResponseEntity<Void> sendMessage(@RequestBody MessageRequestDto dto) {
//        StaffProfileDto myProfile = staffProfileService.getCurrentLoggedInStaffProfile();
//        messageService.sendMessage(myProfile.getUserId(), dto);
//        return ResponseEntity.ok().build();
//    }
//
//
//
//    // --- ADD THESE NEW ENDPOINTS for notifications ---
//    @GetMapping("/notifications")
//    public ResponseEntity<Map<String, Long>> getNotifications() {
//        StaffProfileDto myProfile = staffProfileService.getCurrentLoggedInStaffProfile();
//        long count = messageService.getUnreadMessageCount(myProfile.getBranchId(), UserAccount.Role.valueOf(myProfile.getRole()));
//        return ResponseEntity.ok(Map.of("unreadCount", count));
//    }
//
//    @PostMapping("/mark-as-read/{otherBranchId}")
//    public ResponseEntity<Void> markAsRead(@PathVariable Long otherBranchId) {
//        StaffProfileDto myProfile = staffProfileService.getCurrentLoggedInStaffProfile();
//        messageService.markConversationAsRead(myProfile.getBranchId(), otherBranchId, UserAccount.Role.valueOf(myProfile.getRole()));
//        return ResponseEntity.ok().build();
//    }



//    @GetMapping("/conversation/{otherBranchId}")
//    @PreAuthorize("hasAuthority('ROLE_BRANCH_ADMIN')") // <-- ADD THIS
//    public ResponseEntity<List<MessageDto>> getConversation(@PathVariable Long otherBranchId) {
//        StaffProfileDto myProfile = staffProfileService.getCurrentLoggedInStaffProfile();
//        return ResponseEntity.ok(messageService.getConversation(myProfile.getBranchId(), otherBranchId));
//    }

    @GetMapping("/conversation") // Changed from /conversation/{id}
    @PreAuthorize("hasAuthority('ROLE_BRANCH_ADMIN')")
    public ResponseEntity<List<MessageDto>> getConversation(
            @RequestParam Long otherBranchId,
            @RequestParam UserAccount.Role otherRole) {

        StaffProfileDto myProfile = staffProfileService.getCurrentLoggedInStaffProfile();
        UserAccount.Role myRole = UserAccount.Role.valueOf(myProfile.getRole());

        List<MessageDto> conversation = messageService.getConversation(myProfile.getBranchId(), myRole, otherBranchId, otherRole);

        return ResponseEntity.ok(conversation);
    }

    @PostMapping("/send")
    @PreAuthorize("hasAuthority('ROLE_BRANCH_ADMIN')") // <-- ADD THIS
    public ResponseEntity<Void> sendMessage(@RequestBody MessageRequestDto dto) {
        StaffProfileDto myProfile = staffProfileService.getCurrentLoggedInStaffProfile();
        messageService.sendMessage(myProfile.getUserId(), dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/notifications")
    @PreAuthorize("hasAuthority('ROLE_BRANCH_ADMIN')") // <-- ADD THIS
    public ResponseEntity<Map<String, Long>> getNotifications() {
        StaffProfileDto myProfile = staffProfileService.getCurrentLoggedInStaffProfile();
        long count = messageService.getUnreadMessageCount(myProfile.getBranchId(), UserAccount.Role.valueOf(myProfile.getRole()));
        return ResponseEntity.ok(Map.of("unreadCount", count));
    }

    @PostMapping("/mark-as-read/{otherBranchId}")
    @PreAuthorize("hasAuthority('ROLE_BRANCH_ADMIN')") // <-- ADD THIS
    public ResponseEntity<Void> markAsRead(@PathVariable Long otherBranchId) {
        StaffProfileDto myProfile = staffProfileService.getCurrentLoggedInStaffProfile();
        messageService.markConversationAsRead(myProfile.getBranchId(), otherBranchId, UserAccount.Role.valueOf(myProfile.getRole()));
        return ResponseEntity.ok().build();
    }



    // In MessageApiController.java

    @GetMapping("/contacts")
    @PreAuthorize("hasAuthority('ROLE_BRANCH_ADMIN')")
    public ResponseEntity<List<ContactDto>> getContacts() {
        // 1. Get the profile of the currently logged-in admin
        StaffProfileDto myProfile = staffProfileService.getCurrentLoggedInStaffProfile();

        // 2. Pass their branch ID to the service method
        return ResponseEntity.ok(messageService.getContacts(myProfile.getBranchId()));
    }

// Update your other endpoints to match the new service methods
//    @GetMapping("/conversation/{otherBranchId}")
//    @PreAuthorize("hasAuthority('ROLE_BRANCH_ADMIN')")
//    public ResponseEntity<List<MessageDto>> getConversation(@PathVariable Long otherBranchId) {
//        StaffProfileDto myProfile = staffProfileService.getCurrentLoggedInStaffProfile();
//        List<MessageDto> conversation = messageService.getConversation(myProfile.getBranchId(), otherBranchId);
//        return ResponseEntity.ok(conversation);
//    }

    //    @PostMapping("/send")
    //    @PreAuthorize("hasAuthority('ROLE_BRANCH_ADMIN')")
    //    public ResponseEntity<Void> sendMessage(@RequestBody MessageRequestDto dto) {
    //        StaffProfileDto myProfile = staffProfileService.getCurrentLoggedInStaffProfile();
    //        messageService.sendMessage(myProfile.getBranchId(), dto);
    //        return ResponseEntity.ok().build();
    //    }



    // In MessageApiController.java
//    @PostMapping("/send")
//    public ResponseEntity<Void> sendMessage(@RequestBody MessageRequestDto dto) {
//        StaffProfileDto myProfile = staffProfileService.getCurrentLoggedInStaffProfile();
//        messageService.sendMessage(myProfile.getBranchId(), myProfile.getUserId(), dto);
//        return ResponseEntity.ok().build();
//    }
}