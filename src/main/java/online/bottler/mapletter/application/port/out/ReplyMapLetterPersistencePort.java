package online.bottler.mapletter.application.port.out;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import online.bottler.mapletter.domain.ReplyMapLetter;
import online.bottler.mapletter.application.dto.ReplyProjectDTO;

public interface ReplyMapLetterPersistencePort {
    ReplyMapLetter save(ReplyMapLetter replyMapLetter);

    Page<ReplyMapLetter> findActiveReplyMapLettersBySourceUserId(Long userId, Pageable pageable);

    Page<ReplyMapLetter> findReplyMapLettersBySourceLetterId(Long letterId, Pageable pageable);

    ReplyMapLetter findById(Long letterId);

    boolean findByLetterIdAndUserId(Long letterId, Long userId);

    void letterBlock(Long letterId);

    void softDelete(Long letterId);

    Page<ReplyMapLetter> findAllSentReplyByUserId(Long userId, PageRequest pageRequest);

    List<ReplyProjectDTO> findRecentMapKeywordReplyByUserId(Long userId, int fetchItemSize);

    void softDeleteAllByCreateUserId(Long userId);

    void softDeleteForRecipient(Long letterId);

    void softDeleteAllForRecipient(Long userId);

    List<ReplyMapLetter> findByIds(List<Long> longs);
}
