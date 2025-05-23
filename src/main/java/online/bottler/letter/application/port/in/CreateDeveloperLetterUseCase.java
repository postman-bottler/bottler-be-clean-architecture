package online.bottler.letter.application.port.in;

import online.bottler.letter.application.command.DeveloperLetterCommand;

public interface CreateDeveloperLetterUseCase {
    void create(DeveloperLetterCommand developerLetterCommand);
}
