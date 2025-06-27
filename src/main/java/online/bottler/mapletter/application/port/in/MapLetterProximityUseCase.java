package online.bottler.mapletter.application.port.in;

import java.math.BigDecimal;
import java.util.List;
import online.bottler.mapletter.application.dto.MapLetterAndDistance;
import online.bottler.mapletter.domain.MapLetter;

public interface MapLetterProximityUseCase {
    List<MapLetterAndDistance> findGuestNearByMapLetters(BigDecimal latitude, BigDecimal longitude);

    List<MapLetterAndDistance> findLettersByUserLocation(BigDecimal latitude, BigDecimal longitude, Long userId);

    void validateViewDistance(BigDecimal latitude, BigDecimal longitude, Long letterId, MapLetter mapLetter);

    void validateMapLetterViewPermission(BigDecimal latitude, BigDecimal longitude, Long letterId, MapLetter mapLetter,
                                         Long userId);
}
