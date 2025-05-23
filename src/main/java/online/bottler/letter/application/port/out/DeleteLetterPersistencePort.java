package online.bottler.letter.application.port.out;

import online.bottler.letter.domain.BoxType;

public interface DeleteLetterPersistencePort {
    void softDelete(Long letterId, Long userId, BoxType boxType);
}
