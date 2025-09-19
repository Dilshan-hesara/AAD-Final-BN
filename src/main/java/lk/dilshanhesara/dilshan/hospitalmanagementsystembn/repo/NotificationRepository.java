package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {


    boolean existsByAppointmentId(Long appointmentId);
    List<Notification> findByUser_UsernameAndIsReadFalseOrderByCreatedAtDesc(String username);

}
