package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

    List<Doctor> findByFullNameContainingIgnoreCase(String keyword);

    long countByBranch_Id(Long branchId);


    List<Doctor> findByBranch_Id(Long branchId);


    // Add this method to find the count of ONLY active doctors
    long countByBranch_IdAndStatus(Long branchId, String status);

    // Finds all doctors for a specific branch who have a certain status
    List<Doctor> findByBranch_IdAndStatus(Long branchId, String status);


    @Transactional
    @Modifying
    @Query(value = "UPDATE doctors SET status='INACTIVE' WHERE id=?1", nativeQuery = true)
    void updateDoctorStatusToInactive(int id);

    // --- ADD THIS NEW METHOD TO ACTIVATE ---
    @Transactional
    @Modifying
    @Query(value = "UPDATE doctors SET status='ACTIVE' WHERE id=?1", nativeQuery = true)
    void updateDoctorStatusToActive(int id);


}