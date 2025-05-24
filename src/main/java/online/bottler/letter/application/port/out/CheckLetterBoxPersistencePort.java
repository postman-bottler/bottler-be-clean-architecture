package online.bottler.letter.application.port.out;

public interface CheckLetterBoxPersistencePort {
    boolean existsByLetterIdAndUserId(Long letterId, Long userId);
}
