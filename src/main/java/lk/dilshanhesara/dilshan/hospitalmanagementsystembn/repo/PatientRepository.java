package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByLinkedOnlineUser_UserId(Integer userId);

    @Query("SELECT DISTINCT p FROM Patient p JOIN Appointment a ON p.id = a.patient.id WHERE a.branch.id = :branchId")
    Page<Patient> findPatientsByBranch(Long branchId, Pageable pageable);


    // --- ADD THIS NEW METHOD ---
    // Finds patients where the full name contains the search term (case-insensitive)
    Page<Patient> findByFullNameContainingIgnoreCase(String name, Pageable pageable);


}