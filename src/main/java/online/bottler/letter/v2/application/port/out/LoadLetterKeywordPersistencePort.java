package online.bottler.letter.v2.application.port.out;

import java.util.List;
import online.bottler.letter.domain.LetterKeyword;

public interface LoadLetterKeywordPersistencePort {
    List<LetterKeyword> loadKeywordsByLetterId(Long letterId);
}
