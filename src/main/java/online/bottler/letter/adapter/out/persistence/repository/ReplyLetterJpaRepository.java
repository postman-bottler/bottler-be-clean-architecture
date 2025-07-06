package online.bottler.letter.adapter.out.persistence.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import online.bottler.letter.domain.LetterStatus;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import online.bottler.letter.adapter.out.persistence.entity.ReplyLetterEntity;

public interface ReplyLetterJpaRepository extends JpaRepository<ReplyLetterEntity, Long> {

    @Query("SELECT r FROM ReplyLetterEntity r WHERE r.letterId = :letterId AND r.receiverId = :receiverId AND r.status = :status")
    Page<ReplyLetterEntity> findAllByLetterIdAndReceiverId(Long letterId, Long receiverId, Pageable pageable, LetterStatus status);

    @Override
    @NotNull
    @Query("SELECT r FROM ReplyLetterEntity r WHERE r.id = :id AND r.status = :status")
    Optional<ReplyLetterEntity> findById(@NotNull Long id);

    @Query("SELECT r FROM ReplyLetterEntity r WHERE r.id IN :ids AND r.status = :status")
    List<ReplyLetterEntity> findAllByIds(List<Long> ids);

    @Query("SELECT r.id FROM ReplyLetterEntity r WHERE r.senderId = :senderId AND r.status = :status")
    List<Long> findIdsBySenderId(Long senderId);

    List<ReplyLetterEntity> findAllByIdInAndStatus(Collection<Long> ids, LetterStatus status);

    boolean existsBySenderIdAndLetterId(Long senderId, Long letterId);
}
