package online.bottler.letter.application.port.out;

import java.util.List;

public interface LoadRecommendedLetterIdsCachePort {
    List<Long> fetchActiveByUserId(Long userId);
}
