package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.service;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Appointment;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Notification;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.OnlineUserProfile;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.AppointmentRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.NotificationRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.OnlineUserProfileRepository;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {




    private final AppointmentRepository appointmentRepository;
    private final NotificationRepository notificationRepository;




    // This method runs automatically every 30 minutes
//    @Scheduled(fixedRate = 60000) // 30 minutes in milliseconds
//    @Transactional
//    public void createUpcomingAppointmentNotifications() {
//        System.out.println("SCHEDULER: Checking for upcoming appointments...");
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime in24Hours = now.plusHours(24);
//
//        // Find all appointments scheduled within the next 24 hours
//        List<Appointment> upcomingAppointments = appointmentRepository.findAllByAppointmentDateBetween(now, in24Hours);
//
//        for (Appointment app : upcomingAppointments) {
//            // Check if a notification for this appointment already exists
//            if (app.getPatient().getLinkedOnlineUser() != null && !notificationRepository.existsByAppointmentId(app.getId())) {
//                String message = String.format(
//                        "Reminder: Your appointment with %s at %s is scheduled for today/tomorrow.",
//                        app.getDoctor().getFullName(),
//                        app.getBranch().getName()
//                );
//
//                Notification notification = new Notification();
//                notification.setUser(app.getPatient().getLinkedOnlineUser());
//                notification.setMessage(message);
//                notification.setAppointment(app); // Link to the appointment
//                notificationRepository.save(notification);
//            }
//        }
//    }

//    public List<Notification> getUnreadNotifications(String username) {
//        return notificationRepository.findByUser_UsernameAndIsReadFalseOrderByCreatedAtDesc(username);
//    }
//
//    @Transactional
//    public void markNotificationsAsRead(String username) {
//        List<Notification> unread = getUnreadNotifications(username);
//        unread.forEach(n -> n.setRead(true));
//        notificationRepository.saveAll(unread);
//    }


    public List<Notification> getUnreadNotifications(String username) {
        return notificationRepository.findByUser_UsernameAndIsReadFalseOrderByCreatedAtDesc(username);
    }

    @Transactional
    public void markNotificationsAsRead(String username) {
        // --- CRITICAL FIX: Use the method that finds notifications for the specific user ---
        List<Notification> unread = notificationRepository.findByUser_UsernameAndIsReadFalseOrderByCreatedAtDesc(username);
        if (!unread.isEmpty()) {
            unread.forEach(n -> n.setRead(true));
            notificationRepository.saveAll(unread);
        }
    }


    private final EmailService emailService; // <-- INJECT THE NEW SERVICE

    private final OnlineUserProfileRepository onlineUserProfileRepository; // <-- INJECT THE REPOSITORY

    @Scheduled(fixedRate = 120000) // Runs every 2 minutes for testing
    @Transactional
    public void createUpcomingAppointmentNotifications() {
        System.out.println("--- [SCHEDULER RUNNING] ---");
        LocalDateTime startOfTomorrow = LocalDate.now().plusDays(1).atStartOfDay();
        LocalDateTime endOfTomorrow = startOfTomorrow.plusDays(1).minusSeconds(1);

        List<Appointment> upcomingAppointments = appointmentRepository.findAllByAppointmentDateBetween(startOfTomorrow, endOfTomorrow);

        for (Appointment app : upcomingAppointments) {
            if (app.getPatient().getLinkedOnlineUser() != null && !notificationRepository.existsByAppointmentId(app.getId())) {

                // 1. Create the in-app notification
                String message = String.format(
                        "Reminder: You have an appointment with %s at %s tomorrow.",
                        app.getDoctor().getFullName(), app.getBranch().getName()
                );
                Notification notification = new Notification();
                notification.setUser(app.getPatient().getLinkedOnlineUser());
                notification.setMessage(message);
                notification.setAppointment(app);
                notificationRepository.save(notification);

                // --- 2. Send the Email Notification ---
                OnlineUserProfile profile = onlineUserProfileRepository.findById(app.getPatient().getLinkedOnlineUser().getUserId()).orElse(null);
                if (profile != null && profile.getEmail() != null) {
                    emailService.sendAppointmentReminderEmail(profile.getEmail(), app);
                }
            }
        }
        System.out.println("--- [SCHEDULER FINISHED] ---");
    }



    // branch addmin notification


    private final UserAccountRepository userAccountRepository;

    // --- ADD THIS NEW METHOD ---
    @Transactional
    public void createNewAppointmentNotificationForAdmin(Appointment appointment) {
        userAccountRepository.findBranchAdminByBranchId(appointment.getBranch().getId())
                .ifPresent(branchAdmin -> {
                    String message = String.format(
                            "New online booking from %s for Dr. %s.",
                            appointment.getPatient().getFullName(),
                            appointment.getDoctor().getFullName()
                    );
                    Notification notification = new Notification();
                    notification.setUser(branchAdmin);
                    notification.setMessage(message);
                    notification.setAppointment(appointment);
                    notificationRepository.save(notification);
                });
    }

}
