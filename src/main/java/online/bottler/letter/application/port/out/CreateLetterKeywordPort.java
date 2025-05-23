package online.bottler.letter.application.port.out;

import java.util.List;
import online.bottler.letter.domain.LetterKeyword;

public interface CreateLetterKeywordPort {
    List<LetterKeyword> saveAll(List<LetterKeyword> letterKeywords);
}
