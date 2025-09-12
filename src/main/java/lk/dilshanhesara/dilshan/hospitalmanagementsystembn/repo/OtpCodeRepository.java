package lk.dilshanhesara.dilshan.hospitalmanagementsystembn.repo;

import lk.dilshanhesara.dilshan.hospitalmanagementsystembn.entity.OtpCode;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface OtpCodeRepository extends JpaRepository<OtpCode, Long> {
    Optional<OtpCode> findByUserAccount_UsernameAndOtpCode(String username, String code);
}