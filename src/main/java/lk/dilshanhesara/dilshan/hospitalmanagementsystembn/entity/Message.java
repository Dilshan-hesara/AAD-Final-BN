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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_branch_id", nullable = false)
    private Branch senderBranch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_branch_id", nullable = false)
    private Branch receiverBranch;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp = LocalDateTime.now();
}