package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.Notification;

import java.util.List;

public interface NotificationRepository {



    List<Notification> findByUser_UsernameAndIsReadFalseOrderByCreatedAtDesc(String username);


}
