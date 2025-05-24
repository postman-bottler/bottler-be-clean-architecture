package online.bottler.letter.v2.application.port.in;


import online.bottler.letter.v2.application.command.LetterWithKeywordsCommand;
import online.bottler.letter.v2.application.response.LetterWithKeywordsResponse;

public interface CreateLetterWithKeywordsUseCase {
    LetterWithKeywordsResponse create(LetterWithKeywordsCommand letterWithKeywordsCommand);
}
