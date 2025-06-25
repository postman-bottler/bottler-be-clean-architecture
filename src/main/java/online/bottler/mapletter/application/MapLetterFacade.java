package online.bottler.mapletter.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import online.bottler.mapletter.application.dto.FindReceivedMapLetterDTO;
import online.bottler.mapletter.application.dto.FindSentMapLetter;
import online.bottler.mapletter.application.dto.MapLetterAndDistance;
import online.bottler.mapletter.application.port.in.MapLetterArchiveUseCase;
import online.bottler.mapletter.application.port.in.MapLetterProximityUseCase;
import online.bottler.mapletter.application.port.in.MapLetterReplyUseCase;
import online.bottler.mapletter.application.port.in.MapLetterUseCase;
import online.bottler.mapletter.application.response.FindAllReceivedLetterResponse;
import online.bottler.mapletter.application.response.FindAllSentMapLetterResponse;
import online.bottler.mapletter.application.response.FindMapLetterResponse;
import online.bottler.mapletter.application.response.FindNearbyLettersResponse;
import online.bottler.mapletter.application.response.FindReceivedMapLetterResponse;
import online.bottler.mapletter.application.response.OneLetterResponse;
import online.bottler.mapletter.domain.MapLetter;
import online.bottler.user.application.port.in.UserUseCase;
import org.springframework.data.domain.Page;
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
        MapLetter mapLetter = mapLetterUseCase.findArchiveOneLetterById(letterId, userId);
        return getOneLetterResponse(letterId, userId, mapLetter);
    }

    public List<FindNearbyLettersResponse> guestFindNearByMapLetters(BigDecimal latitude, BigDecimal longitude) {
        List<MapLetterAndDistance> letters = mapLetterProximityUseCase.findGuestNearByMapLetters(latitude, longitude);

        Set<Long> userIds = mapLetterProximityUseCase.extractCreateUserIds(letters);
        Map<Long, String> nicknamesByIds = userUseCase.getNicknamesByIds(userIds);

        return mapLetterProximityUseCase.convertToFindNearbyResponses(letters, nicknamesByIds);
    }

    public OneLetterResponse guestFindOneMapLetter(Long letterId, BigDecimal latitude, BigDecimal longitude) {
        MapLetter mapLetter = mapLetterUseCase.findPublicMapLetterById(letterId);
        mapLetterProximityUseCase.validateViewDistance(latitude, longitude, letterId, mapLetter);
        String profileImg = userUseCase.getProfileImageUrlById(mapLetter.getCreateUserId());
        return OneLetterResponse.from(mapLetter, profileImg, false, false, false);
    }

    public OneLetterResponse findOneMapLetter(Long letterId, Long userId, BigDecimal latitude, BigDecimal longitude) {
        MapLetter mapLetter = mapLetterUseCase.findById(letterId);

        mapLetterProximityUseCase.validateMapLetterViewPermission(latitude, longitude, letterId, mapLetter, userId);
        mapLetterUseCase.targetUserUpdateRead(userId, mapLetter);

        return getOneLetterResponse(letterId, userId, mapLetter);
    }

    public List<FindNearbyLettersResponse> findNearByMapLetters(BigDecimal latitude, BigDecimal longitude,
                                                                Long userId) {
        List<MapLetterAndDistance> letters = mapLetterProximityUseCase.findLettersByUserLocation(latitude, longitude,
                userId);

        Set<Long> userIds = mapLetterProximityUseCase.extractCreateUserIds(letters);
        Map<Long, String> nicknamesByIds = userUseCase.getNicknamesByIds(userIds);
        return mapLetterProximityUseCase.convertToFindNearbyResponses(letters, nicknamesByIds);
    }

    public Page<FindMapLetterResponse> findSentMapLetters(int page, int size, Long userId) {
        Page<FindSentMapLetter> sentLetters = mapLetterUseCase.findSentLetters(page, size, userId);

        Set<Long> userIds = mapLetterUseCase.extractTargetUserIdsBySentMapLetters(sentLetters);
        Map<Long, String> nicknamesByIds = userUseCase.getNicknamesByIds(userIds);

        return mapLetterUseCase.convertToFindMapLetterResponse(sentLetters, nicknamesByIds);
    }

    public Page<FindReceivedMapLetterResponse> findReceivedMapLetters(int page, int size, Long userId) {
        Page<FindReceivedMapLetterDTO> letters = mapLetterUseCase.findReceivedMapLetters(page, size, userId);

        Set<Long> userIds = mapLetterUseCase.extractSentUserIdsByReceivedMapLetters(letters);
        Map<Long, String> nicknamesByIds = userUseCase.getNicknamesByIds(userIds);
        Map<Long, String> profileImageUrlsByIds = userUseCase.getProfileImageUrlsByIds(userIds);

        return mapLetterUseCase.convertToFindReceivedMapLetterResponse(letters, nicknamesByIds, profileImageUrlsByIds);
    }

    public Page<FindAllSentMapLetterResponse> findAllSentMapLetters(int page, int size, Long userId) {
        Page<MapLetter> letters = mapLetterUseCase.findActiveByCreateUserId(page, size, userId);

        Set<Long> userIds = mapLetterUseCase.extractTargetUserIdsByMapLetters(letters);
        Map<Long, String> nicknamesByIds = userUseCase.getNicknamesByIds(userIds);

        return mapLetterUseCase.convertToFindAllSentMapLetterResponse(letters, nicknamesByIds);
    }

    public Page<FindAllReceivedLetterResponse> findAllReceivedLetters(int page, int size, Long userId) {
        Page<MapLetter> letters = mapLetterUseCase.findActiveLetter(page, size, userId);

        Set<Long> userIds = mapLetterUseCase.extractCreateUserIdsByMapLetters(letters);
        Map<Long, String> nicknamesByIds = userUseCase.getNicknamesByIds(userIds);
        Map<Long, String> profileImageUrlsByIds = userUseCase.getProfileImageUrlsByIds(userIds);

        return mapLetterUseCase.convertToFindAllReceivedLetterResponse(letters, nicknamesByIds, profileImageUrlsByIds);
    }

    private OneLetterResponse getOneLetterResponse(Long letterId, Long userId, MapLetter mapLetter) {
        String profileImg = userUseCase.getProfileImageUrlById(mapLetter.getCreateUserId());
        boolean isCreated = mapLetter.isCreated(userId);
        boolean isReplied = mapLetterReplyUseCase.checkReplyMapLetter(letterId, userId).isReplied();
        boolean isArchived = mapLetterArchiveUseCase.isArchived(letterId, userId);

        return OneLetterResponse.from(mapLetter, profileImg, isCreated, isReplied, isArchived);
    }
}
