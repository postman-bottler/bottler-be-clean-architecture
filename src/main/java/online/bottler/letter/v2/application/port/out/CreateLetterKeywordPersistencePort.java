package online.bottler.letter.v2.application.port.out;

import java.util.List;
import online.bottler.letter.domain.LetterKeyword;

public interface CreateLetterKeywordPersistencePort {
    List<LetterKeyword> createAll(List<LetterKeyword> letterKeywords);
}
