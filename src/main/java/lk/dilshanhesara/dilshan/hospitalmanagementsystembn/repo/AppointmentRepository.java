package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo;


import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByPatient_Id(Long patientId);

    List<Appointment> findByDoctor_Id(Integer doctorId);


    long countByBranch_IdAndAppointmentDateBetween(Long branchId, LocalDateTime startOfDay, LocalDateTime endOfDay);
}