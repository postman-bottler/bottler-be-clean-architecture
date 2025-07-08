package online.bottler.letter.application.port.out;

import java.util.List;
import java.util.Optional;
import online.bottler.letter.domain.Letter;
import online.bottler.letter.domain.LetterStatus;

public interface LetterPersistencePort {
    Letter save(Letter letter);

    Optional<Letter> loadByIdAndStatus(Long letterId, LetterStatus status);

    List<Long> loadRandomLetterIdsByIdNotInAndStatus(List<Long> excludedIds, LetterStatus status, int count);

    boolean existsByIdAndStatus(Long letterId, LetterStatus status);

    List<Letter> loadAllByIdIn(List<Long> letterIds);

    List<Letter> loadAllByIdInAndStatus(List<Long> ids, LetterStatus letterStatus);

    List<Long> loadIdsByUserIdAndStatus(Long userId, LetterStatus status);

    void saveAll(List<Letter> letters);
}
