package online.bottler.letter.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import online.bottler.letter.adapter.out.persistence.entity.LetterBoxEntity;

public interface LetterBoxJpaRepository extends JpaRepository<LetterBoxEntity, Long> {
    boolean existsByUserIdAndLetterId(Long userId, Long letterId);
}
