package online.bottler.mapletter.application.port.in;

import java.math.BigDecimal;
import java.util.List;
import online.bottler.mapletter.application.response.FindNearbyLettersResponse;
import online.bottler.mapletter.application.response.OneLetterResponse;

public interface MapLetterProximityUseCase {
    OneLetterResponse findOneMapLetter(Long letterId, Long userId, BigDecimal latitude, BigDecimal longitude);

    List<FindNearbyLettersResponse> findNearByMapLetters(BigDecimal latitude, BigDecimal longitude, Long userId);
}
