package online.bottler.letter.application.port.out;

public interface CheckLetterBoxPort {
    boolean existsByLetterIdAndUserId(Long letterId, Long userId);
}
