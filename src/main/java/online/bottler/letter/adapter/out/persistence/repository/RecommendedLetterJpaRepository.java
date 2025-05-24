package online.bottler.letter.adapter.out.persistence.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import online.bottler.letter.adapter.out.persistence.entity.RecommendedLetterEntity;

public interface RecommendedLetterJpaRepository extends JpaRepository<RecommendedLetterEntity, Long> {
    @Query("SELECT rl.letterId FROM RecommendedLetterEntity rl WHERE rl.userId = :userId")
    List<Long> findRecommendedLetterIdsByUserId(Long userId);
}
