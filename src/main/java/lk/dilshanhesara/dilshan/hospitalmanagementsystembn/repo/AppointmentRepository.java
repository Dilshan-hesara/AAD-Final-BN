package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo;


import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.AppointmentRequestDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository  extends JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {

    List<Appointment> findByPatient_Id(Long patientId);

    List<Appointment> findByDoctor_Id(Integer doctorId);


    long countByBranch_IdAndAppointmentDateBetween(Long branchId, LocalDateTime startOfDay, LocalDateTime endOfDay);




    /**
     * Finds all appointments for a specific branch.
     * @param branchId The ID of the branch.
     * @return A list of appointments.
     */
    List<Appointment> findByBranch_Id(Long branchId);

    // --- ADD THIS NEW METHOD ---
    // This JPQL query finds appointments by joining through Patient and UserAccount tables


    // --- ADD THIS NEW METHOD ---
    @Query("SELECT a FROM Appointment a WHERE a.patient.linkedOnlineUser.username = :username")
    List<Appointment> findAppointmentsByOnlineUsername(String username);



    // Add this method to find appointments for a branch made by online users within a time range
    @Query("SELECT a FROM Appointment a WHERE a.branch.id = :branchId AND a.patient.linkedOnlineUser IS NOT NULL AND a.appointmentDate BETWEEN :startOfDay AND :endOfDay ORDER BY a.appointmentDate ASC")
    List<Appointment> findOnlineUserAppointmentsForToday(Long branchId, LocalDateTime startOfDay, LocalDateTime endOfDay);

    // --- ADD THIS NEW METHOD ---
    // Counts appointments by branch, status, and within a date range
    long countByBranch_IdAndStatusAndAppointmentDateBetween(Long branchId, String status, LocalDateTime start, LocalDateTime end);



    // In AppointmentRepository.java
// Finds all appointments with a specific status for a branch within a date range
    List<Appointment> findByBranch_IdAndStatusAndAppointmentDateBetween(Long branchId, String status, LocalDateTime start, LocalDateTime end);




    // Counts appointments made by Online Users for today
    @Query("SELECT count(a) FROM Appointment a WHERE a.branch.id = :branchId AND a.patient.linkedOnlineUser IS NOT NULL AND a.appointmentDate BETWEEN :startOfDay AND :endOfDay")
    long countTodaysOnlineAppointments(Long branchId, LocalDateTime startOfDay, LocalDateTime endOfDay);

    // Counts appointments made by patients WITHOUT a linked online account for today
    @Query("SELECT count(a) FROM Appointment a WHERE a.branch.id = :branchId AND a.patient.linkedOnlineUser IS NULL AND a.appointmentDate BETWEEN :startOfDay AND :endOfDay")
    long countTodaysWalkInAppointments(Long branchId, LocalDateTime startOfDay, LocalDateTime endOfDay);



    // Add this method to count all appointments ever for a branch
    long countByBranch_Id(Long branchId);
    // Add this method to count today's appointments for a branch


}
