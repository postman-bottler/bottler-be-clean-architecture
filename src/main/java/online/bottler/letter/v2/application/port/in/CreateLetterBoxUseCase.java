package online.bottler.letter.v2.application.port.in;


import online.bottler.letter.v2.application.command.LetterBoxCommand;

public interface CreateLetterBoxUseCase {
    void create(LetterBoxCommand letterBoxCommand);
}
