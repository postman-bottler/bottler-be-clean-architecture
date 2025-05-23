package online.bottler.mapletter.application;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.mapletter.application.dto.MapLetterAndDistance;
import online.bottler.mapletter.application.port.in.MapLetterProximityUseCase;
import online.bottler.mapletter.application.port.out.MapLetterPersistencePort;
import online.bottler.mapletter.application.response.FindNearbyLettersResponse;
import online.bottler.mapletter.application.response.OneLetterResponse;
import online.bottler.mapletter.domain.MapLetter;
import online.bottler.user.application.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MapLetterProximityService implements MapLetterProximityUseCase {

    private final MapLetterPersistencePort mapLetterPersistencePort;
    private final MapLetterReplyService mapLetterReplyService;
    private final MapLetterArchiveService mapLetterArchiveService;
    private final UserService userService;

    private static final double VIEW_DISTANCE = 15;

    @Override
    @Transactional
    public OneLetterResponse findOneMapLetter(Long letterId, Long userId, BigDecimal latitude, BigDecimal longitude) {
        MapLetter mapLetter = mapLetterPersistencePort.findById(letterId);

        Double distance = mapLetterPersistencePort.findDistanceByLatitudeAndLongitudeAndLetterId(
                latitude, longitude, letterId);

        mapLetter.validateFindOneMapLetter(VIEW_DISTANCE, distance);
        mapLetter.validateAccess(userId);

        if (mapLetter.isTargetUser(userId)) {
            mapLetterPersistencePort.updateRead(mapLetter);
        }

        String profileImg = userService.getProfileImageUrlById(mapLetter.getCreateUserId());
        return OneLetterResponse.from(mapLetter, profileImg, mapLetter.getCreateUserId() == userId,
                mapLetterReplyService.checkReplyMapLetter(letterId, userId).isReplied(),
                mapLetterArchiveService.isArchived(letterId, userId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<FindNearbyLettersResponse> findNearByMapLetters(
            BigDecimal latitude, BigDecimal longitude, Long userId) {
        List<MapLetterAndDistance> letters = mapLetterPersistencePort.findLettersByUserLocation(latitude, longitude,
                userId);

        return letters.stream()
                .map(letter -> {
                            String nickname = userService.getNicknameById(letter.getCreateUserId());
                            return FindNearbyLettersResponse.from(letter, nickname);
                        }
                ).toList();
    }
}
