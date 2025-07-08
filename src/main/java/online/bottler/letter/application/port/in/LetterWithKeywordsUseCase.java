package online.bottler.letter.application.port.in;

import java.util.List;
import online.bottler.letter.application.command.LetterWithKeywordsCommand;
import online.bottler.letter.application.command.LetterWithKeywordsDetailQuery;
import online.bottler.letter.domain.Letter;
import online.bottler.letter.domain.LetterWithKeywords;

public interface LetterWithKeywordsUseCase {
    Letter write(LetterWithKeywordsCommand command);

    LetterWithKeywords getLetterWithKeywords(LetterWithKeywordsDetailQuery query);

    String getLabel(Long letterId);

    List<Long> getLetterIdsByUserId(Long userId);

    List<Letter> getLettersIncludingAllStatusByIdIn(List<Long> recommendedLetterIds);

    void deleteLetter(Long userId, Long letterId);

    void deleteLetters(Long userId, List<Long> letterIds);
}
