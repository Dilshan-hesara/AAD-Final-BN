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




    // --- ADD THIS NEW METHOD ---
    // This query checks both staff and online user profiles for a matching email
    @Query("SELECT ua FROM UserAccount ua " +
            "LEFT JOIN StaffProfile sp ON ua.userId = sp.userId " +
            "LEFT JOIN OnlineUserProfile oup ON ua.userId = oup.userId " +
            "WHERE sp.email = :email OR oup.email = :email")
    Optional<UserAccount> findByEmail(String email);


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




    // seper admin dash

    long countByRole(UserAccount.Role role);
    long countByRoleAndIsActive(UserAccount.Role role, boolean isActive);


    @Query("SELECT ua FROM UserAccount ua JOIN ua.staffProfile sp WHERE ua.role = :role")
    Page<UserAccount> findByRoleWithProfile(UserAccount.Role role, Pageable pageable);


    @Query("SELECT ua FROM UserAccount ua JOIN ua.staffProfile sp WHERE ua.role = 'BRANCH_ADMIN'")
    Page<UserAccount> findBranchAdmins(Pageable pageable);



    Page<UserAccount> findByRole(UserAccount.Role role, Pageable pageable);
    // --- METHOD 1: Pagination සමඟින් user ලැයිස්තුවක් ලබාගැනීමට ---

    // --- METHOD 2: Pagination නොමැතිව, සම්පූර්ණ user ලැයිස්තුවක් ලබාගැනීමට ---
    List<UserAccount> findAllByRole(UserAccount.Role role);
    // --- METHOD 2: To count the total number of users with a specific role ---



    @Query("SELECT ua FROM UserAccount ua JOIN ua.staffProfile sp WHERE sp.branch.id = :branchId AND ua.role = 'BRANCH_ADMIN'")
    List<UserAccount> findBranchAdminInBranch(Long branchId);

    /**
     * Finds all Receptionist user accounts in a specific branch, excluding the current user.
     */
    @Query("SELECT ua FROM UserAccount ua JOIN ua.staffProfile sp WHERE sp.branch.id = :branchId AND ua.role = 'RECEPTIONIST' AND ua.userId != :myUserId")
    List<UserAccount> findColleaguesInBranch(Long branchId, Integer myUserId);



    // Finds all users who are either a BRANCH_ADMIN or a RECEPTIONIST
    @Query("SELECT ua FROM UserAccount ua WHERE ua.role IN ('BRANCH_ADMIN', 'RECEPTIONIST')")
    List<UserAccount> findAllStaffUsers();




    @Query("SELECT ua FROM UserAccount ua JOIN ua.staffProfile sp WHERE sp.branch.id = :branchId AND ua.role = :role")
    List<UserAccount> findUserInBranchByRole(Long branchId, UserAccount.Role role);






}