package online.bottler.letter.v2.application.port.out;

import online.bottler.letter.domain.BoxType;

public interface DeleteLetterPersistencePort {
    void softDelete(Long letterId, Long userId, BoxType boxType);
}
