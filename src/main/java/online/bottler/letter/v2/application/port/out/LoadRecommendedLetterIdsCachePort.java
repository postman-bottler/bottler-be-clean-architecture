package online.bottler.letter.v2.application.port.out;

import java.util.List;

public interface LoadRecommendedLetterIdsCachePort {
    List<Long> fetchActiveByUserId(Long userId);
}
