package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "user_accounts")
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password; // This will store the hashed password

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "is_active", columnDefinition = "boolean default false")
    private boolean isActive;

    public enum Role {
        SUPER_ADMIN,
        BRANCH_ADMIN,
        RECEPTIONIST,
        ONLINEUSER
    }
}