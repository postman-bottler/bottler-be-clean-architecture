package online.bottler.letter.adapter.out.persistence.repository;

import java.util.Collection;
import java.util.List;
import online.bottler.letter.domain.LetterStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import online.bottler.letter.adapter.out.persistence.entity.LetterKeywordEntity;

public interface LetterKeywordJpaRepository extends JpaRepository<LetterKeywordEntity, Long> {
    List<LetterKeywordEntity> findAllByLetterIdInAndStatus(Collection<Long> letterIds, LetterStatus status);
}
