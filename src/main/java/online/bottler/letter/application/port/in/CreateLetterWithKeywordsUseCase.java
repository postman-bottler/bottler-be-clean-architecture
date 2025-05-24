package online.bottler.letter.application.port.in;

import online.bottler.letter.application.command.LetterWithKeywordsCommand;
import online.bottler.letter.application.response.LetterWithKeywordsResponse;

public interface CreateLetterWithKeywordsUseCase {
    LetterWithKeywordsResponse create(LetterWithKeywordsCommand letterWithKeywordsCommand);
}
