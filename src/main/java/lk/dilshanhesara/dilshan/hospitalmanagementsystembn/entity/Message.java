package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "messages")
public class Message {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "sender_branch_id", nullable = false)
//    private Branch senderBranch;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "receiver_branch_id", nullable = false)
//    private Branch receiverBranch;
//
//    @Lob
//    @Column(nullable = false)
//    private String content;
//
//    @Column(nullable = false, updatable = false)
//    private LocalDateTime timestamp = LocalDateTime.now();
//
//
//    @ManyToOne
//    @JoinColumn(name = "sender_user_id", nullable = false)
//    private UserAccount sender; // The actual user who sent the message



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "sender_user_id", nullable = false)
    private UserAccount sender;

    @ManyToOne @JoinColumn(name = "sender_branch_id", nullable = false)
    private Branch senderBranch;

    @ManyToOne @JoinColumn(name = "receiver_branch_id", nullable = false)
    private Branch receiverBranch;

    @Enumerated(EnumType.STRING)
    private UserAccount.Role recipientRole; // e.g., BRANCH_ADMIN, RECEPTIONIST

    @Lob
    private String content;

    private boolean isRead = false; // For notifications

    private LocalDateTime timestamp = LocalDateTime.now();
}