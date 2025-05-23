package online.bottler.letter.application.port.out;

public interface CheckReplyLetterPort {
    boolean existsByLetterIdAndUserId(Long letterId, Long userId);
}
