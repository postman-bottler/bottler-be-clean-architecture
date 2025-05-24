package online.bottler.user.adapter.out.persistence.repository;

import java.util.Optional;
import online.bottler.user.adapter.out.persistence.entity.EmailCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailCodeJpaRepository extends JpaRepository<EmailCodeEntity, Long> {
    EmailCodeEntity findByEmail(String email);
    Optional<EmailCodeEntity> findByEmailAndCode(String email, String code);
    void deleteByEmail(String email);
}
