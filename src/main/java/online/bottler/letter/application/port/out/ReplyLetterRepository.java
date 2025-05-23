package online.bottler.letter.application.port.out;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import online.bottler.letter.domain.ReplyLetter;

public interface ReplyLetterRepository {
    ReplyLetter save(ReplyLetter replyLetter);

    Page<ReplyLetter> findAllByLetterIdAndReceiverId(Long letterId, Long receiverId, Pageable pageable);

    Optional<ReplyLetter> findById(Long replyLetterId);

    List<ReplyLetter> findAllByIds(List<Long> letterIds);

    List<ReplyLetter> findAllBySenderId(Long senderId);

    void softDeleteByIds(List<Long> letterIds);

    void softBlockById(Long replyLetterId);

    boolean existsByLetterIdAndSenderId(Long letterId, Long senderId);

    boolean existsByIdAndSenderId(Long replyLetterId, Long userId);
}
