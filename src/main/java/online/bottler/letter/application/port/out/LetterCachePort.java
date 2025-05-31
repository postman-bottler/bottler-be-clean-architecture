package online.bottler.letter.application.port.out;

import java.util.List;

public interface LetterCachePort {
    List<Long> fetchActiveByUserId(Long userId);
}
