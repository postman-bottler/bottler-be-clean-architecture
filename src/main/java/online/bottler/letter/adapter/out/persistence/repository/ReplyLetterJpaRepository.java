package online.bottler.letter.adapter.out.persistence.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import online.bottler.letter.domain.LetterStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import online.bottler.letter.adapter.out.persistence.entity.ReplyLetterEntity;

public interface ReplyLetterJpaRepository extends JpaRepository<ReplyLetterEntity, Long> {

    Page<ReplyLetterEntity> findAllByReceiverIdAndLetterIdAndStatus(Long receiverId, Long letterId, LetterStatus status, Pageable pageable);

    Optional<ReplyLetterEntity> findByIdAndStatus(Long id, LetterStatus status);

    @Query("SELECT r FROM ReplyLetterEntity r WHERE r.id IN :ids AND r.status = :status")
    List<ReplyLetterEntity> findAllByIds(List<Long> ids);

    List<Long> findIdsBySenderIdAndStatus(Long senderId, LetterStatus status);

    List<ReplyLetterEntity> findAllByIdInAndStatus(Collection<Long> ids, LetterStatus status);

    boolean existsBySenderIdAndLetterId(Long senderId, Long letterId);
}
