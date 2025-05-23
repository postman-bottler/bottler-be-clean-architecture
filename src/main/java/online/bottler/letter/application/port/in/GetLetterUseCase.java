package online.bottler.letter.application.port.in;

import online.bottler.letter.domain.Letter;

public interface GetLetterUseCase {
    Letter getLetter(String letterId);
}
