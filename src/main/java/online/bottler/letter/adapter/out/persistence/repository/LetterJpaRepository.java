package online.bottler.letter.adapter.out.persistence.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import online.bottler.letter.domain.LetterStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import online.bottler.letter.adapter.out.persistence.entity.LetterEntity;

public interface LetterJpaRepository extends JpaRepository<LetterEntity, Long> {

    Optional<LetterEntity> findByIdAndStatus(Long id, LetterStatus status);

    List<LetterEntity> findAllByIdIn(Collection<Long> ids);

    List<LetterEntity> findAllByIdInAndStatus(Collection<Long> ids, LetterStatus status);

    List<Long> findIdsByUserIdAndStatus(Long userId, LetterStatus status);

    @Query(
            """
                SELECT MAX(l.id)
                FROM LetterEntity l
                WHERE l.status = :status
            """
    )
    Optional<Long> findMaxIdByStatus(LetterStatus status);

    @Query(
            """
                SELECT l.id
                FROM LetterEntity l
                WHERE l.status = :status AND l.id >= :randomId AND l.id NOT IN :excludedIds
                ORDER BY l.id
                LIMIT :count
            """
    )
    List<Long> findIdsByIdNotInAndStatus(int count, Long randomId, Collection<Long> excludedIds, LetterStatus status);

    boolean existsByIdAndStatus(Long id, LetterStatus status);
}
