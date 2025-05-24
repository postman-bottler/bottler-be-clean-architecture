package online.bottler.letter.application.port.out;

public interface CheckReplyLetterPersistencePort {
    boolean existsByLetterIdAndUserId(Long letterId, Long userId);
}
