package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount user;

    @Column(nullable = false)
    private String message;

    @Column(name = "is_read")
    private boolean isRead = false;

    private LocalDateTime createdAt = LocalDateTime.now();

    // Link to the appointment to prevent duplicate notifications
    @OneToOne
    @JoinColumn(name = "appointment_id", unique = true)
    private Appointment appointment;
}