package online.bottler.letter.application.port.in;

import online.bottler.letter.application.command.LetterWithKeywordsCommand;
import online.bottler.letter.application.command.LetterWithKeywordsDeleteCommand;
import online.bottler.letter.application.command.LetterWithKeywordsDetailQuery;
import online.bottler.letter.application.response.LetterWithKeywordsDetailResponse;
import online.bottler.letter.application.response.LetterWithKeywordsResponse;

public interface LetterWithKeywordsUseCase {
    LetterWithKeywordsResponse create(LetterWithKeywordsCommand command);

    LetterWithKeywordsDetailResponse getDetail(LetterWithKeywordsDetailQuery query);

    String getLabel(Long letterId);

    void delete(LetterWithKeywordsDeleteCommand command);
}
