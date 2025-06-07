package online.bottler.mapletter.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import online.bottler.mapletter.application.dto.MapLetterAndDistance;
import online.bottler.mapletter.application.port.in.MapLetterArchiveUseCase;
import online.bottler.mapletter.application.port.in.MapLetterProximityUseCase;
import online.bottler.mapletter.application.port.in.MapLetterReplyUseCase;
import online.bottler.mapletter.application.port.in.MapLetterUseCase;
import online.bottler.mapletter.application.response.FindNearbyLettersResponse;
import online.bottler.mapletter.application.response.OneLetterResponse;
import online.bottler.mapletter.domain.MapLetter;
import online.bottler.mapletter.domain.policy.MapLetterPolicy;
import online.bottler.user.application.port.in.UserUseCase;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MapLetterFacade {

    private final MapLetterArchiveUseCase mapLetterArchiveUseCase;
    private final MapLetterUseCase mapLetterUseCase;
    private final UserUseCase userUseCase;
    private final MapLetterReplyUseCase mapLetterReplyUseCase;
    private final MapLetterProximityUseCase mapLetterProximityUseCase;

    public OneLetterResponse findArchiveOneLetter(Long letterId, Long userId) {
        MapLetter mapLetter = mapLetterUseCase.findById(letterId);
        mapLetter.validateAccess(userId);

        String profileImg = userUseCase.getProfileImageUrlById(mapLetter.getCreateUserId());
        boolean isReplied = mapLetterReplyUseCase.checkReplyMapLetter(letterId, userId).isReplied();
        boolean isArchived = mapLetterArchiveUseCase.isArchived(letterId, userId);
        boolean isCreated = mapLetter.getCreateUserId().equals(userId);

        return OneLetterResponse.from(mapLetter, profileImg, isCreated, isReplied, isArchived);
    }

    public List<FindNearbyLettersResponse> guestFindNearByMapLetters(BigDecimal latitude, BigDecimal longitude) {
        List<MapLetterAndDistance> letters = mapLetterProximityUseCase.findGuestNearByMapLetters(latitude, longitude);

        List<Long> userIds = letters.stream()
                .map(MapLetterAndDistance::getCreateUserId)
                .distinct()
                .toList();

        Map<Long, String> nicknamesByIds = userUseCase.getNicknamesByIds(userIds);

        return letters.stream()
                .map(letter -> {
                    String nickname = nicknamesByIds.get(letter.getCreateUserId());
                    return FindNearbyLettersResponse.from(letter, nickname);
                })
                .toList();
    }

    public OneLetterResponse guestFindOneMapLetter(Long letterId, BigDecimal latitude, BigDecimal longitude) {
        MapLetter mapLetter = mapLetterUseCase.findById(letterId);
        mapLetter.validatePublicAccess();

        Double distance = mapLetterProximityUseCase.findDistance(latitude, longitude, letterId);
        mapLetter.validateFindOneMapLetter(MapLetterPolicy.VIEW_DISTANCE, distance);

        String profileImg = userUseCase.getProfileImageUrlById(mapLetter.getCreateUserId());
        return OneLetterResponse.from(mapLetter, profileImg, false, false, false);
    }
}
