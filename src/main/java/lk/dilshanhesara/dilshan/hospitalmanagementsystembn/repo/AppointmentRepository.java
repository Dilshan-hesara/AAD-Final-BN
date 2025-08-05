package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo;


import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

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
    @Query("SELECT a FROM Appointment a WHERE a.patient.linkedOnlineUser.username = :username")
    List<Appointment> findAppointmentsByOnlineUsername(String username);

}