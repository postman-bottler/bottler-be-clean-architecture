package online.bottler.mapletter.application.port.in;

import java.math.BigDecimal;
import java.util.List;
import online.bottler.mapletter.application.dto.MapLetterAndDistance;

public interface MapLetterProximityUseCase {
    Double findDistance(BigDecimal latitude, BigDecimal longitude, Long letterId);

    List<MapLetterAndDistance> findGuestNearByMapLetters(BigDecimal latitude, BigDecimal longitude);

    List<MapLetterAndDistance> findLettersByUserLocation(BigDecimal latitude, BigDecimal longitude, Long userId);
}
