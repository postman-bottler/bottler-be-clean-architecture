package online.bottler.letter.application.port.out;

import java.util.List;
import online.bottler.letter.domain.LetterKeyword;

public interface CreateLetterKeywordPersistencePort {
    List<LetterKeyword> createAll(List<LetterKeyword> letterKeywords);
}
