package online.bottler.letter.application.port.out;

import java.util.List;
import java.util.Optional;
import online.bottler.letter.domain.LetterStatus;
import online.bottler.letter.domain.ReplyLetter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReplyLetterPersistencePort {
    ReplyLetter save(ReplyLetter replyLetter);

    void saveAll(List<ReplyLetter> replyLetters);

    Optional<ReplyLetter> loadByIdAndStatus(Long id, LetterStatus status);

    List<ReplyLetter> loadAllByIdInAndStatus(List<Long> ids, LetterStatus status);

    Page<ReplyLetter> loadAllByReceiverIdAndLetterIdAndStatus(Long receiverId, Long letterId, LetterStatus status, Pageable pageable);

    List<Long> loadIdsByUserIdAndStatus(Long userId, LetterStatus status);

    boolean existsBySenderIdAndLetterId(Long userId, Long letterId);
}
