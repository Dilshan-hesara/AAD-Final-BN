package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo;


import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.AppointmentRequestDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.DailyAppointmentStatDto;
import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.TopDoctorDto;
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



    List<Appointment> findByBranch_Id(Long branchId);

    // This JPQL query finds appointments by joining through Patient and UserAccount tables


    @Query("SELECT a FROM Appointment a WHERE a.patient.linkedOnlineUser.username = :username")
    List<Appointment> findAppointmentsByOnlineUsername(String username);



    // Add this method to find appointments for a branch made by online users within a time range
    @Query("SELECT a FROM Appointment a WHERE a.branch.id = :branchId AND a.patient.linkedOnlineUser IS NOT NULL AND a.appointmentDate BETWEEN :startOfDay AND :endOfDay ORDER BY a.appointmentDate ASC")
    List<Appointment> findOnlineUserAppointmentsForToday(Long branchId, LocalDateTime startOfDay, LocalDateTime endOfDay);

    // Counts appointments by branch, status, and within a date range
    long countByBranch_IdAndStatusAndAppointmentDateBetween(Long branchId, String status, LocalDateTime start, LocalDateTime end);



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




    // Add these methods to your AppointmentRepository interface
    long countByAppointmentDateBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT count(a) FROM Appointment a WHERE a.patient.linkedOnlineUser IS NOT NULL AND a.appointmentDate BETWEEN :start AND :end")
    long countOnlineAppointmentsBetween(LocalDateTime start, LocalDateTime end);

    long countByStatusAndAppointmentDateBetween(String status, LocalDateTime start, LocalDateTime end);





//    @Query("SELECT new TopDoctorDto(d.fullName, COUNT(a.id)) " +
//            "FROM Appointment a JOIN a.doctor d " +
//            "WHERE a.branch.id = :branchId AND a.appointmentDate BETWEEN :startDate AND :endDate " +
//            "GROUP BY d.fullName ORDER BY COUNT(a.id) DESC")
//    List<TopDoctorDto> findTopDoctorsByBranch(Long branchId, LocalDateTime startDate, LocalDateTime endDate);
//
//    @Query("SELECT new DailyAppointmentStatDto(FUNCTION('DATE_FORMAT', a.appointmentDate, '%Y-%m-%d'), COUNT(a.id)) " +
//            "FROM Appointment a " +
//            "WHERE a.branch.id = :branchId AND a.appointmentDate BETWEEN :startDate AND :endDate " +
//            "GROUP BY FUNCTION('DATE_FORMAT', a.appointmentDate, '%Y-%m-%d')")
//    List<DailyAppointmentStatDto> findDailyAppointmentStats(Long branchId, LocalDateTime startDate, LocalDateTime endDate);
//

//
    @Query("SELECT new lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.TopDoctorDto(d.fullName, COUNT(a.id)) " +
            "FROM Appointment a JOIN a.doctor d " +
            "WHERE a.branch.id = :branchId AND a.appointmentDate BETWEEN :startDate AND :endDate " +
            "GROUP BY d.fullName ORDER BY COUNT(a.id) DESC")
    List<TopDoctorDto> findTopDoctorsByBranch(Long branchId, LocalDateTime startDate, LocalDateTime endDate);

//    @Query("SELECT new lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.DailyAppointmentStatDto(FUNCTION('DATE_FORMAT', a.appointmentDate, '%Y-%m-%d'), COUNT(a.id)) " +
//            "FROM Appointment a " +
//            "WHERE a.branch.id = :branchId AND a.appointmentDate BETWEEN :startDate AND :endDate " +
//            "GROUP BY FUNCTION('DATE_FORMAT', a.appointmentDate, '%Y-%m-%d')")
//    List<DailyAppointmentStatDto> findDailyAppointmentStats(Long branchId, LocalDateTime startDate, LocalDateTime endDate);

    @Query(value = "SELECT DATE_FORMAT(a.appointment_date, '%Y-%m-%d') as date, COUNT(a.id) as count " +
            "FROM appointment a " +
            "WHERE a.branch_id = :branchId " +
            "AND a.appointment_date BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE_FORMAT(a.appointment_date, '%Y-%m-%d')",
            nativeQuery = true)
    List<Object[]> findDailyAppointmentStats(Long branchId, LocalDateTime startDate, LocalDateTime endDate);

}
