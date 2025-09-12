package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "otp_codes")
public class OtpCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount userAccount;
    private String otpCode;
    private LocalDateTime expiryTime;
}