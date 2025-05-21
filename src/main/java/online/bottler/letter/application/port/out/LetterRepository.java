package online.bottler.letter.application.port.out;

import java.util.List;
import java.util.Optional;
import online.bottler.letter.domain.Letter;

public interface LetterRepository {
    Letter save(Letter letter);

    Optional<Letter> findById(Long letterId);

    List<Letter> findAllByIds(List<Long> letterIds);

    List<Letter> findAllByUserId(Long userId);

    void softDeleteByIds(List<Long> letterIds);

    void softBlockById(Long letterId);

    boolean existsById(Long letterId);

    List<Long> getRandomIds(int count, List<Long> excludedLetterIds);
}
