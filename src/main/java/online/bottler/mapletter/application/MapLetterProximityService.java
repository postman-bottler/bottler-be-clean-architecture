package online.bottler.mapletter.application;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.mapletter.application.dto.MapLetterAndDistance;
import online.bottler.mapletter.application.port.in.MapLetterProximityUseCase;
import online.bottler.mapletter.application.port.out.MapLetterPersistencePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MapLetterProximityService implements MapLetterProximityUseCase {

    private final MapLetterPersistencePort mapLetterPersistencePort;

    @Override
    @Transactional(readOnly = true)
    public List<MapLetterAndDistance> findLettersByUserLocation(BigDecimal latitude, BigDecimal longitude,
                                                                Long userId) {
        return mapLetterPersistencePort.findLettersByUserLocation(latitude, longitude, userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Double findDistance(BigDecimal latitude, BigDecimal longitude, Long letterId) {
        return mapLetterPersistencePort.findDistanceByLatitudeAndLongitudeAndLetterId(latitude, longitude, letterId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MapLetterAndDistance> findGuestNearByMapLetters(BigDecimal latitude, BigDecimal longitude) {
        return mapLetterPersistencePort.guestFindLettersByUserLocation(latitude, longitude);
    }
}
