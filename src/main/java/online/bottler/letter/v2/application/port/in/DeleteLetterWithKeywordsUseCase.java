package online.bottler.letter.v2.application.port.in;


import online.bottler.letter.v2.application.command.LetterWithKeywordsDeleteCommand;

public interface DeleteLetterWithKeywordsUseCase {
    void delete(LetterWithKeywordsDeleteCommand command);
}
