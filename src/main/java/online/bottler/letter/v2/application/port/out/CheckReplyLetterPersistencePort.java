package online.bottler.letter.v2.application.port.out;

public interface CheckReplyLetterPersistencePort {
    boolean existsByLetterIdAndUserId(Long letterId, Long userId);
}
