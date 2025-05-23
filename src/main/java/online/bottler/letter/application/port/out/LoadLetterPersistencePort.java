package online.bottler.letter.application.port.out;

import java.util.List;
import java.util.Optional;
import online.bottler.letter.domain.Letter;

public interface LoadLetterPersistencePort {
    Optional<Letter> loadById(Long letterId);

    List<Letter> loadAllByIds(List<Long> letterIds);
}
