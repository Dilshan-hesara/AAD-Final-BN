package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.controller;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.MessageDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.MessageRequestDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.StaffProfileDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.MessageService;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.StaffProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageApiController {
    private final MessageService messageService;
    private final StaffProfileService staffProfileService;

    @GetMapping("/conversation/{otherBranchId}")
    @PreAuthorize("hasAuthority('ROLE_BRANCH_ADMIN')")
    public ResponseEntity<List<MessageDto>> getConversation(@PathVariable Long otherBranchId) {
        StaffProfileDto myProfile = staffProfileService.getCurrentLoggedInStaffProfile();
        List<MessageDto> conversation = messageService.getConversation(myProfile.getBranchId(), otherBranchId);
        return ResponseEntity.ok(conversation);
    }

    @PostMapping("/send")
    @PreAuthorize("hasAuthority('ROLE_BRANCH_ADMIN')")
    public ResponseEntity<Void> sendMessage(@RequestBody MessageRequestDto dto) {
        StaffProfileDto myProfile = staffProfileService.getCurrentLoggedInStaffProfile();
        messageService.sendMessage(myProfile.getBranchId(), dto);
        return ResponseEntity.ok().build();
    }
}