package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Integer>, JpaSpecificationExecutor<UserAccount> {

    Optional<UserAccount> findByUsername(String username);




    // ... your existing findByUsername method

    // NEW METHOD: Finds all user accounts with the role RECEPTIONIST for a given branch ID
//
//    @Query("SELECT ua FROM UserAccount ua JOIN StaffProfile sp ON ua.userId = sp.userId WHERE ua.role = 'RECEPTIONIST' AND sp.branch.id = :branchId")
//    List<UserAccount> findReceptionistsByBranch(Long branchId);
//
//
//    // Add this method to find the count of receptionists for a branch
//    @Query("SELECT count(ua) FROM UserAccount ua JOIN StaffProfile sp ON ua.userId = sp.userId WHERE ua.role = 'RECEPTIONIST' AND sp.branch.id = :branchId")
//    long countReceptionistsByBranch(Long branchId);
//

    @Query("SELECT ua FROM UserAccount ua JOIN ua.staffProfile sp WHERE ua.role = 'RECEPTIONIST' AND sp.branch.id = :branchId")
    List<UserAccount> findReceptionistsByBranch(Long branchId);



    @Query("SELECT count(ua) FROM UserAccount ua JOIN ua.staffProfile sp WHERE ua.role = 'RECEPTIONIST' AND sp.branch.id = :branchId AND ua.isActive = true")
    long countReceptionistsByBranch(Long branchId);

    @Query("SELECT count(ua) FROM UserAccount ua JOIN StaffProfile sp ON ua.userId = sp.userId WHERE ua.role = 'RECEPTIONIST' AND sp.branch.id = :branchId AND ua.isActive = true")
    long countActiveReceptionistsByBranch(Long branchId);


}