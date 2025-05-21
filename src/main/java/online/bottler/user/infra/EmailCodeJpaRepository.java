package online.bottler.user.infra;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import online.bottler.user.infra.entity.EmailCodeEntity;

public interface EmailCodeJpaRepository extends JpaRepository<EmailCodeEntity, Long> {
    EmailCodeEntity findByEmail(String email);
    Optional<EmailCodeEntity> findByEmailAndCode(String email, String code);
    void deleteByEmail(String email);
}
