package online.bottler.letter.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import online.bottler.letter.infra.entity.KeywordEntity;

public interface KeywordJpaRepository extends JpaRepository<KeywordEntity, Long> {
}
