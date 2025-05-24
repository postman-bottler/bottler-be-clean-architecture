package online.bottler.letter.v2.application.port.out;

public interface DeleteLetterKeywordPersistencePort {
    void softDelete(Long letterId);
}
