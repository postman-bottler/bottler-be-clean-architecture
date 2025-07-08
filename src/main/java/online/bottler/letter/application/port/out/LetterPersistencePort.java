package online.bottler.letter.application.port.out;

import java.util.List;
import java.util.Optional;
import online.bottler.letter.domain.Letter;
import online.bottler.letter.domain.LetterStatus;

public interface LetterPersistencePort {
    Letter save(Letter letter);

    Optional<Letter> loadByIdAndStatus(Long letterId, LetterStatus status);

    List<Long> fetchRandomLetterIdsExcluding(int count, List<Long> excludedIds);

    boolean existsById(Long letterId);

    List<Letter> loadAllByIdIn(List<Long> letterIds);

    List<Letter> loadAllByIdInAndStatus(List<Long> ids, LetterStatus letterStatus);

    List<Long> loadIdsByUserIdAndStatus(Long userId, LetterStatus status);

    void saveAll(List<Letter> letters);
}
