package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.controller;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Notification;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationApiController {

    private final NotificationService notificationService;

    @GetMapping("/unread")
    public ResponseEntity<List<Notification>> getUnread() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(notificationService.getUnreadNotifications(username));
    }

    @PostMapping("/mark-as-read")
    public ResponseEntity<Void> markAsRead() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        notificationService.markNotificationsAsRead(username);
        return ResponseEntity.ok().build();
    }
}