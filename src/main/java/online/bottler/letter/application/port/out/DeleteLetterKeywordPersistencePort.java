package online.bottler.letter.application.port.out;

public interface DeleteLetterKeywordPersistencePort {
    void softDelete(Long letterId);
}
