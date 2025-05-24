package online.bottler.letter.application;

import lombok.RequiredArgsConstructor;
import online.bottler.letter.application.command.DeveloperLetterCommand;
import online.bottler.letter.application.port.in.CreateDeveloperLetterUseCase;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisLetterServiceV2 implements CreateDeveloperLetterUseCase {

    @Override
    public void create(DeveloperLetterCommand developerLetterCommand) {

    }
}
