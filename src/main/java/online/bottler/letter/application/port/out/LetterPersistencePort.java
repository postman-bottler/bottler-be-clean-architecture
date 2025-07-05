package online.bottler.letter.application.port.out;

import java.util.List;
import java.util.Optional;
import online.bottler.letter.domain.Letter;
import online.bottler.letter.domain.LetterStatus;

public interface LetterPersistencePort {
    Letter create(Letter letter);

    Optional<Letter> loadById(Long letterId);

    List<Letter> loadAllIncludingDeletedByIds(List<Long> letterIds);

    List<Long> loadIdsByUserId(Long userId);

    List<Long> fetchRandomLetterIdsExcluding(int count, List<Long> excludedIds);

    boolean existsById(Long letterId);

    List<Letter> loadAllByIdInAndStatus(List<Long> ids, LetterStatus letterStatus);

    void createAll(List<Letter> letters);
}
