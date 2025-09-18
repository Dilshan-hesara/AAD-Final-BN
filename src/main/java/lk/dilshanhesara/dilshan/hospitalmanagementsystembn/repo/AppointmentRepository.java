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
import java.util.Optional;

@Repository
public interface AppointmentRepository  extends JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {

    List<Appointment> findByPatient_Id(Long patientId);

    List<Appointment> findByDoctor_Id(Integer doctorId);


    long countByBranch_IdAndAppointmentDateBetween(Long branchId, LocalDateTime startOfDay, LocalDateTime endOfDay);


    List<Appointment> findByBranch_Id(Long branchId);


    @Query("SELECT a FROM Appointment a WHERE a.branch.id = :branchId AND a.patient.linkedOnlineUser IS NOT NULL AND a.appointmentDate BETWEEN :startOfDay AND :endOfDay ORDER BY a.appointmentDate ASC")
    List<Appointment> findOnlineUserAppointmentsForToday(Long branchId, LocalDateTime startOfDay, LocalDateTime endOfDay);

    long countByBranch_IdAndStatusAndAppointmentDateBetween(Long branchId, String status, LocalDateTime start, LocalDateTime end);


    List<Appointment> findByBranch_IdAndStatusAndAppointmentDateBetween(Long branchId, String status, LocalDateTime start, LocalDateTime end);



    @Query("SELECT count(a) FROM Appointment a WHERE a.branch.id = :branchId AND a.patient.linkedOnlineUser IS NOT NULL AND a.appointmentDate BETWEEN :startOfDay AND :endOfDay")
    long countTodaysOnlineAppointments(Long branchId, LocalDateTime startOfDay, LocalDateTime endOfDay);

    @Query("SELECT count(a) FROM Appointment a WHERE a.branch.id = :branchId AND a.patient.linkedOnlineUser IS NULL AND a.appointmentDate BETWEEN :startOfDay AND :endOfDay")
    long countTodaysWalkInAppointments(Long branchId, LocalDateTime startOfDay, LocalDateTime endOfDay);



    long countByBranch_Id(Long branchId);


    long countByAppointmentDateBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT count(a) FROM Appointment a WHERE a.patient.linkedOnlineUser IS NOT NULL AND a.appointmentDate BETWEEN :start AND :end")
    long countOnlineAppointmentsBetween(LocalDateTime start, LocalDateTime end);

    long countByStatusAndAppointmentDateBetween(String status, LocalDateTime start, LocalDateTime end);






    @Query("SELECT new lk.dilshanhesara.dilshan.hospitalmanagementsystembn.dto.TopDoctorDto(d.fullName, COUNT(a.id)) " +
            "FROM Appointment a JOIN a.doctor d " +
            "WHERE a.branch.id = :branchId AND a.appointmentDate BETWEEN :startDate AND :endDate " +
            "GROUP BY d.fullName ORDER BY COUNT(a.id) DESC")
    List<TopDoctorDto> findTopDoctorsByBranch(Long branchId, LocalDateTime startDate, LocalDateTime endDate);


    @Query(value = "SELECT DATE_FORMAT(a.appointment_date, '%Y-%m-%d') as date, COUNT(a.id) as count " +
            "FROM appointment a " +
            "WHERE a.branch_id = :branchId " +
            "AND a.appointment_date BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE_FORMAT(a.appointment_date, '%Y-%m-%d')",
            nativeQuery = true)
    List<Object[]> findDailyAppointmentStats(Long branchId, LocalDateTime startDate, LocalDateTime endDate);


    Optional<Appointment> findTopByPatientIdOrderByAppointmentDateDesc(Long patientId);




    @Query("SELECT a FROM Appointment a WHERE a.patient.linkedOnlineUser.username = :username ORDER BY a.appointmentDate DESC")
    List<Appointment> findAppointmentsByOnlineUsername(String username);


    @Query("SELECT a FROM Appointment a WHERE a.patient.linkedOnlineUser.username = :username AND a.appointmentDate >= :now ORDER BY a.appointmentDate ASC")
    List<Appointment> findUpcomingAppointmentsByUsername(String username, LocalDateTime now);


    @Query("SELECT a FROM Appointment a WHERE a.patient.linkedOnlineUser.username = :username AND a.appointmentDate < :now ORDER BY a.appointmentDate DESC")
    List<Appointment> findRecentAppointmentsByUsername(String username, LocalDateTime now);



    List<Appointment> findAllByAppointmentDateBetween(LocalDateTime start, LocalDateTime end);
}

