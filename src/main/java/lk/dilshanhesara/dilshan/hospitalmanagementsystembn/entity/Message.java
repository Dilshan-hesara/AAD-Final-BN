package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "messages")
public class Message {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne @JoinColumn(name = "sender_user_id", nullable = false)
    private UserAccount sender;


    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private UserAccount receiver;

    @ManyToOne @JoinColumn(name = "sender_branch_id")
    private Branch senderBranch;

    @ManyToOne @JoinColumn(name = "receiver_branch_id")
    private Branch receiverBranch;

    @Enumerated(EnumType.STRING)
    private UserAccount.Role recipientRole;

    @Lob
    private String content;
    private boolean isRead = false;
    private LocalDateTime timestamp = LocalDateTime.now();








}