package online.bottler.letter.application.port.in;

import online.bottler.letter.application.command.LetterWithKeywordsDeleteCommand;

public interface DeleteLetterWithKeywordsUseCase {
    void delete(LetterWithKeywordsDeleteCommand command);
}
