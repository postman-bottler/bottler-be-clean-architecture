package online.bottler.letter.application.port.out;

import online.bottler.letter.domain.Letter;

public interface CreateLetterPersistencePort {
    Letter create(Letter letter);
}
