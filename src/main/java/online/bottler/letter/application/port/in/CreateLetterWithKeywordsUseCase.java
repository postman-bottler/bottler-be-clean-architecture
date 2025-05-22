package online.bottler.letter.application.port.in;

import online.bottler.letter.application.command.LetterWithKeywordsCommand;
import online.bottler.letter.application.response.LetterResponse;

public interface CreateLetterWithKeywordsUseCase {
    LetterResponse create(LetterWithKeywordsCommand letterWithKeywordsCommand);
}
