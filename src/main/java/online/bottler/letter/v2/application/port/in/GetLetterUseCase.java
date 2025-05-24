package online.bottler.letter.v2.application.port.in;

import online.bottler.letter.domain.Letter;

public interface GetLetterUseCase {
    Letter getLetter(String letterId);
}
