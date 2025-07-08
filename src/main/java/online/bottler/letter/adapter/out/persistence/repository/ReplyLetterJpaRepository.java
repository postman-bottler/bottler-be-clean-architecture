package online.bottler.letter.adapter.out.persistence.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import online.bottler.letter.domain.LetterStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import online.bottler.letter.adapter.out.persistence.entity.ReplyLetterEntity;

public interface ReplyLetterJpaRepository extends JpaRepository<ReplyLetterEntity, Long> {

    Optional<ReplyLetterEntity> findByIdAndStatus(Long id, LetterStatus status);

    List<ReplyLetterEntity> findAllByIdInAndStatus(Collection<Long> ids, LetterStatus status);

    Page<ReplyLetterEntity> findAllByReceiverIdAndLetterIdAndStatus(Long receiverId, Long letterId, LetterStatus status, Pageable pageable);

    List<Long> findIdsBySenderIdAndStatus(Long senderId, LetterStatus status);

    boolean existsBySenderIdAndLetterId(Long senderId, Long letterId);
}
