package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface DoctorRepository  extends JpaRepository<Doctor, Integer>, JpaSpecificationExecutor<Doctor> {

    List<Doctor> findByFullNameContainingIgnoreCase(String keyword);

    long countByBranch_Id(Long branchId);


    List<Doctor> findByBranch_Id(Long branchId);


    long countByBranch_IdAndStatus(Long branchId, String status);

    List<Doctor> findByBranch_IdAndStatus(Long branchId, String status);


    @Transactional
    @Modifying
    @Query(value = "UPDATE doctors SET status='INACTIVE' WHERE id=?1", nativeQuery = true)
    void updateDoctorStatusToInactive(int id);
    @Transactional
    @Modifying
    @Query(value = "UPDATE doctors SET status='ACTIVE' WHERE id=?1", nativeQuery = true)
    void updateDoctorStatusToActive(int id);

    Page<Doctor> findByBranch_IdAndStatusAndFullNameContainingIgnoreCase(Long branchId, String status, String name, Pageable pageable);


    long countByStatus(String status);


}