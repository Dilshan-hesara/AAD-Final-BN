package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "branches")
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String location;

    @Column(name = "contact_number")
    private String contactNumber;

    private String status;

    private String email;

}