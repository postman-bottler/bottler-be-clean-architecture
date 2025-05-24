package online.bottler.user.adapter.out.persistence.repository;

import java.util.Optional;
import online.bottler.user.adapter.out.persistence.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByEmail(String email);
    Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);
    void deleteByEmail(String email);
}
