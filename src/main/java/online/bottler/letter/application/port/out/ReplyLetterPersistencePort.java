package online.bottler.letter.application.port.out;

import java.util.List;
import java.util.Optional;
import online.bottler.letter.domain.LetterStatus;
import online.bottler.letter.domain.ReplyLetter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReplyLetterPersistencePort {
    boolean existsBySenderIdAndLetterId(Long userId, Long letterId);

    ReplyLetter save(ReplyLetter replyLetter);

    Page<ReplyLetter> loadAllByReceiverIdAndLetterIdAndStatus(Long receiverId, Long letterId, LetterStatus status, Pageable pageable);

    Optional<ReplyLetter> loadByIdAndStatus(Long id, LetterStatus status);

    List<Long> loadIdsByUserIdAndStatus(Long userId, LetterStatus status);

    List<ReplyLetter> loadAllByIds(List<Long> ids);

    void saveAll(List<ReplyLetter> replyLetters);

    List<ReplyLetter> loadAllByIdInAndStatus(List<Long> ids, LetterStatus status);
}
