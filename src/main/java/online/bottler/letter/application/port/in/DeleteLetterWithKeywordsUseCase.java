package online.bottler.letter.application.port.in;

import online.bottler.letter.application.command.LetterDeleteDTO;

public interface DeleteLetterWithKeywordsUseCase {
    void delete(LetterDeleteDTO letterDeleteDTO, Long userId);
}
