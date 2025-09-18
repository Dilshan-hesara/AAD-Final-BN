package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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


    @Query("SELECT ua FROM UserAccount ua " +
            "LEFT JOIN StaffProfile sp ON ua.userId = sp.userId " +
            "LEFT JOIN OnlineUserProfile oup ON ua.userId = oup.userId " +
            "WHERE sp.email = :email OR oup.email = :email")
    Optional<UserAccount> findByEmail(String email);




    @Query("SELECT ua FROM UserAccount ua JOIN ua.staffProfile sp WHERE ua.role = 'RECEPTIONIST' AND sp.branch.id = :branchId")
    List<UserAccount> findReceptionistsByBranch(Long branchId);



    @Query("SELECT count(ua) FROM UserAccount ua JOIN ua.staffProfile sp WHERE ua.role = 'RECEPTIONIST' AND sp.branch.id = :branchId AND ua.isActive = true")
    long countReceptionistsByBranch(Long branchId);

    @Query("SELECT count(ua) FROM UserAccount ua JOIN StaffProfile sp ON ua.userId = sp.userId WHERE ua.role = 'RECEPTIONIST' AND sp.branch.id = :branchId AND ua.isActive = true")
    long countActiveReceptionistsByBranch(Long branchId);




    // seper admin dash

    long countByRole(UserAccount.Role role);
    long countByRoleAndIsActive(UserAccount.Role role, boolean isActive);


    @Query("SELECT ua FROM UserAccount ua JOIN ua.staffProfile sp WHERE ua.role = :role")
    Page<UserAccount> findByRoleWithProfile(UserAccount.Role role, Pageable pageable);


    @Query("SELECT ua FROM UserAccount ua JOIN ua.staffProfile sp WHERE ua.role = 'BRANCH_ADMIN'")
    Page<UserAccount> findBranchAdmins(Pageable pageable);



    Page<UserAccount> findByRole(UserAccount.Role role, Pageable pageable);

    List<UserAccount> findAllByRole(UserAccount.Role role);



    @Query("SELECT ua FROM UserAccount ua JOIN ua.staffProfile sp WHERE sp.branch.id = :branchId AND ua.role = 'BRANCH_ADMIN'")
    List<UserAccount> findBranchAdminInBranch(Long branchId);

    @Query("SELECT ua FROM UserAccount ua JOIN ua.staffProfile sp WHERE sp.branch.id = :branchId AND ua.role = 'RECEPTIONIST' AND ua.userId != :myUserId")
    List<UserAccount> findColleaguesInBranch(Long branchId, Integer myUserId);



    // Finds all users who are either a BRANCH_ADMIN or a RECEPTIONIST
    @Query("SELECT ua FROM UserAccount ua WHERE ua.role IN ('BRANCH_ADMIN', 'RECEPTIONIST')")
    List<UserAccount> findAllStaffUsers();




    @Query("SELECT ua FROM UserAccount ua JOIN ua.staffProfile sp WHERE sp.branch.id = :branchId AND ua.role = :role")
    List<UserAccount> findUserInBranchByRole(Long branchId, UserAccount.Role role);



    @Query("SELECT ua FROM UserAccount ua JOIN ua.staffProfile sp WHERE sp.branch.id = :branchId AND ua.role = 'BRANCH_ADMIN'")
    Optional<UserAccount> findBranchAdminByBranchId(Long branchId);


}