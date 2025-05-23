package online.bottler.letter.application.port.out;

import online.bottler.letter.domain.Letter;

public interface CreateLetterPort {
    Letter create(Letter letter);
}
