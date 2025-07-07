package online.bottler.letter.application.port.out;

import java.util.List;
import java.util.Optional;
import online.bottler.letter.domain.LetterStatus;
import online.bottler.letter.domain.ReplyLetter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReplyLetterPersistencePort {
    boolean existsByUserIdAndLetterId(Long userId, Long letterId);

    ReplyLetter save(ReplyLetter replyLetter);

    Page<ReplyLetter> loadSummariesByLetterIdAndReceiverId(Long letterId, Long receiverId, Pageable pageable);

    Optional<ReplyLetter> loadById(Long id);

    List<Long> loadIdsByUserId(Long userId);

    List<ReplyLetter> loadAllByIds(List<Long> ids);

    void createAll(List<ReplyLetter> replyLetters);

    List<ReplyLetter> loadAllByIdInAndStatus(List<Long> ids, LetterStatus status);
}
