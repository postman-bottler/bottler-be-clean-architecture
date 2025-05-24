package online.bottler.letter.v2.application.port.out;

public interface CheckLetterBoxPersistencePort {
    boolean existsByLetterIdAndUserId(Long letterId, Long userId);
}
