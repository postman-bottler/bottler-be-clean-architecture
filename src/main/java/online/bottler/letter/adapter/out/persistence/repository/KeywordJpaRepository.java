package online.bottler.letter.adapter.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import online.bottler.letter.adapter.out.persistence.entity.KeywordEntity;

public interface KeywordJpaRepository extends JpaRepository<KeywordEntity, Long> {
}
