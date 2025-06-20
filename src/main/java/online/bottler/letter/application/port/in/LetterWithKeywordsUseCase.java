package online.bottler.letter.application.port.in;

import java.util.List;
import online.bottler.letter.application.command.LetterWithKeywordsCommand;
import online.bottler.letter.application.command.LetterWithKeywordsDeleteCommand;
import online.bottler.letter.application.command.LetterWithKeywordsDetailQuery;
import online.bottler.letter.domain.Letter;
import online.bottler.letter.domain.LetterWithKeywords;

public interface LetterWithKeywordsUseCase {
    Letter create(LetterWithKeywordsCommand command);

    LetterWithKeywords get(LetterWithKeywordsDetailQuery query);

    String getLabel(Long letterId);

    void delete(LetterWithKeywordsDeleteCommand command);

    Letter getLetter(Long letterId);

    List<Long> getLetterIdsByUserId(Long userId);

    List<Letter> loadAllIncludingDeletedByIds(List<Long> recommendedLetterIds);

    void softDeleteByIds(List<Long> ids);
}
