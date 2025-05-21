package online.bottler.letter.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import online.bottler.letter.infra.entity.LetterBoxEntity;

public interface LetterBoxJpaRepository extends JpaRepository<LetterBoxEntity, Long> {
    boolean existsByUserIdAndLetterId(Long userId, Long letterId);
}
