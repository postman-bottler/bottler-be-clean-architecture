package online.bottler.letter.application.port.in;

import online.bottler.letter.application.command.LetterWithKeywordsCommand;
import online.bottler.letter.application.response.LetterResponseDTO;

public interface CreateLetterWithKeywordsUseCase {
    LetterResponseDTO create(LetterWithKeywordsCommand letterWithKeywordsCommand);
}
