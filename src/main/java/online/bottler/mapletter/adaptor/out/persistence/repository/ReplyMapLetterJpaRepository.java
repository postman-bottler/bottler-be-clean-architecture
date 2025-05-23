package online.bottler.mapletter.adaptor.out.persistence.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import online.bottler.mapletter.application.dto.ReplyProjectDTO;
import online.bottler.mapletter.adaptor.out.persistence.entity.ReplyMapLetterEntity;

@Repository
public interface ReplyMapLetterJpaRepository extends JpaRepository<ReplyMapLetterEntity, Long> {

    @Query("SELECT r FROM ReplyMapLetterEntity r, MapLetterEntity m WHERE m.createUserId = :userId " +
            "AND m.mapLetterId = r.sourceLetterId AND r.isDeleted=false AND r.isBlocked = false AND r.isRecipientDeleted = false ORDER BY r.createdAt DESC ")
    Page<ReplyMapLetterEntity> findActiveReplyMapLettersBySourceUserId(Long userId, Pageable pageable);

    @Query("SELECT r FROM ReplyMapLetterEntity r " +
            "WHERE r.sourceLetterId=:letterId AND r.isDeleted=false AND r.isBlocked = false")
    Page<ReplyMapLetterEntity> findReplyMapLettersBySourceLetterId(Long letterId, Pageable pageable);

    Optional<ReplyMapLetterEntity> findBySourceLetterIdAndCreateUserId(Long letterId, Long userId);

    @Modifying
    @Query("UPDATE ReplyMapLetterEntity r SET r.isBlocked = true WHERE r.replyLetterId = :letterId")
    void letterBlock(Long letterId);

    @Query("SELECT r FROM ReplyMapLetterEntity r WHERE r.createUserId=:userId AND r.isBlocked=false AND r.isDeleted=false"
            + " ORDER BY r.createdAt DESC")
    Page<ReplyMapLetterEntity> findAllSentReplyByUserId(Long userId, PageRequest pageRequest);

    @Query(value = """
                SELECT id, label, created_at, type
                FROM(
                    SELECT r.reply_letter_id as id, r.created_at, r.label, 'MAP' as type
                    FROM reply_map_letter r
                    JOIN map_letter m ON r.source_letter_id = m.map_letter_id
                    WHERE m.create_user_id=:userId AND r.is_deleted=false AND r.is_blocked = false 
                        UNION ALL
                    SELECT rl.id AS id, rl.created_at, rl.label, 'KEYWORD' as type
                    FROM reply_letters rl
                    WHERE rl.receiver_id=:userId AND rl.is_blocked=false AND rl.is_deleted=false 
                ) combined
                ORDER BY created_at DESC
                LIMIT :fetchItemSize
            """, nativeQuery = true)
    List<ReplyProjectDTO> findRecentMapKeywordReplyByUserId(Long userId, int fetchItemSize);

    List<ReplyMapLetterEntity> findAllByCreateUserId(Long createUserId);

    @Query(value = """
                 SELECT r FROM MapLetterEntity m, ReplyMapLetterEntity r
                 WHERE m.mapLetterId = r.sourceLetterId AND m.createUserId=:userId
            """)
    List<ReplyMapLetterEntity> findAllBySourceLetterCreateUserId(Long userId);
}
