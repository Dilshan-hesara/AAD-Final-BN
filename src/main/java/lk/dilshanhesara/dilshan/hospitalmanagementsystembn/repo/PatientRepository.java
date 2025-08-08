package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByLinkedOnlineUser_UserId(Integer userId);

}