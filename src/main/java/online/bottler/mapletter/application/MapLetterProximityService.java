package online.bottler.mapletter.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import online.bottler.mapletter.application.dto.MapLetterAndDistance;
import online.bottler.mapletter.application.port.in.MapLetterProximityUseCase;
import online.bottler.mapletter.application.port.out.MapLetterPersistencePort;
import online.bottler.mapletter.application.response.FindNearbyLettersResponse;
import online.bottler.mapletter.domain.MapLetter;
import online.bottler.mapletter.domain.policy.MapLetterPolicy;
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
    public Set<Long> extractCreateUserIds(List<MapLetterAndDistance> letters) {
        return letters.stream()
                .map(MapLetterAndDistance::getCreateUserId)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MapLetterAndDistance> findGuestNearByMapLetters(BigDecimal latitude, BigDecimal longitude) {
        return mapLetterPersistencePort.guestFindLettersByUserLocation(latitude, longitude);
    }

    @Override
    public List<FindNearbyLettersResponse> convertToFindNearbyResponses(List<MapLetterAndDistance> letters,
                                                                        Map<Long, String> nicknamesByIds) {
        return letters.stream()
                .map(letter -> {
                    String nickname = nicknamesByIds.get(letter.getCreateUserId());
                    return FindNearbyLettersResponse.from(letter, nickname);
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public void validateViewDistance(BigDecimal latitude, BigDecimal longitude, Long letterId, MapLetter mapLetter) {
        Double distance = findDistance(latitude, longitude, letterId);
        mapLetter.validateFindOneMapLetter(MapLetterPolicy.VIEW_DISTANCE, distance);
    }

    @Override
    @Transactional(readOnly = true)
    public void validateMapLetterViewPermission(BigDecimal latitude, BigDecimal longitude, Long letterId,
                                                MapLetter mapLetter, Long userId) {
        Double distance = findDistance(latitude, longitude, letterId);
        mapLetter.validateFindOneMapLetter(MapLetterPolicy.VIEW_DISTANCE, distance);
        mapLetter.validateAccess(userId);
    }

    private Double findDistance(BigDecimal latitude, BigDecimal longitude, Long letterId) {
        return mapLetterPersistencePort.findDistanceByLatitudeAndLongitudeAndLetterId(latitude, longitude, letterId);
    }
}
