package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.OnlineUserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OnlineUserProfileRepository extends JpaRepository<OnlineUserProfile, Integer> {

    Optional<OnlineUserProfile> findByUserAccount_Username(String username);


}