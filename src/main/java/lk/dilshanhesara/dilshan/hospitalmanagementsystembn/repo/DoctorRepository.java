package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
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




}