package online.bottler.letter.application.port.out;

import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.LetterType;

public interface DeleteLetterBoxPersistencePort {
    void delete(Long letterId, LetterType letterType, BoxType boxType);
}
