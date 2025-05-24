package online.bottler.letter.application.port.in;

import online.bottler.letter.application.command.LetterBoxCommand;

public interface CreateLetterBoxUseCase {
    void create(LetterBoxCommand letterBoxCommand);
}
