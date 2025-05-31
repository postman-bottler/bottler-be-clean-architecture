package online.bottler.letter.application.port.out;

import java.util.List;
import java.util.Optional;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.Letter;

public interface LetterPersistencePort {
    Letter create(Letter letter);

    Optional<Letter> loadById(Long letterId);

    List<Letter> loadAllByIds(List<Long> letterIds);

    List<Letter> loadAllByUserId(Long userId);

    List<Long> loadIdsByUserId(Long userId);

    List<Long> fetchRandomLetterIdsExcluding(int count, List<Long> excludedIds);

    void softDelete(Long letterId, Long userId, BoxType boxType);

    void softDeleteByIds(List<Long> letterIds);

    void softBlock(Long id);

    boolean existsById(Long letterId);
}
