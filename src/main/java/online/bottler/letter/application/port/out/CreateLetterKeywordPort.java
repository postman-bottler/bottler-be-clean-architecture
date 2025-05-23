package online.bottler.letter.application.port.out;

import java.util.List;
import online.bottler.letter.domain.LetterKeyword;

public interface CreateLetterKeywordPort {
    void saveAll(List<LetterKeyword> letterKeywords);
}
