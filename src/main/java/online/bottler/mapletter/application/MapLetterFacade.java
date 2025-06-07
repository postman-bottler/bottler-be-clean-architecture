package online.bottler.mapletter.application;

import static online.bottler.mapletter.application.DeleteLetterType.MAP;
import static online.bottler.mapletter.application.DeleteLetterType.REPLY;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
import online.bottler.mapletter.application.validator.PageValidator;
import online.bottler.mapletter.domain.MapLetter;
import online.bottler.mapletter.domain.MapLetterType;
import online.bottler.mapletter.domain.policy.MapLetterPolicy;
import online.bottler.user.application.port.in.UserUseCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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

    public OneLetterResponse findOneMapLetter(Long letterId, Long userId, BigDecimal latitude, BigDecimal longitude) {
        MapLetter mapLetter = mapLetterUseCase.findById(letterId);
        Double distance = mapLetterProximityUseCase.findDistance(latitude, longitude, letterId);

        mapLetter.validateFindOneMapLetter(MapLetterPolicy.VIEW_DISTANCE, distance);
        mapLetter.validateAccess(userId);

        mapLetterUseCase.targetUserUpdateRead(userId, mapLetter);

        String profileImg = userUseCase.getProfileImageUrlById(mapLetter.getCreateUserId());
        boolean isReplied = mapLetterReplyUseCase.hasReplyForMapLetter(letterId, userId).isReplied();
        boolean isArchived = mapLetterArchiveUseCase.isArchived(letterId, userId);

        return OneLetterResponse.from(mapLetter, profileImg, Objects.equals(mapLetter.getCreateUserId(), userId),
                isReplied, isArchived);
    }

    public List<FindNearbyLettersResponse> findNearByMapLetters(BigDecimal latitude, BigDecimal longitude,
                                                                Long userId) {
        List<MapLetterAndDistance> letters = mapLetterProximityUseCase.findLettersByUserLocation(latitude, longitude,
                userId);

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

    public Page<FindMapLetterResponse> findSentMapLetters(int page, int size, Long userId) {
        PageValidator.validMinPage(page);

        Page<FindSentMapLetter> sentLetters = mapLetterUseCase.findSentLetters(page, size, userId);

        if (sentLetters.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(page - 1, size), 0);
        }

        PageValidator.validMaxPage(sentLetters.getTotalPages(), page);

        return sentLetters.map(this::toFindSentMapLetter);
    }

    private FindMapLetterResponse toFindSentMapLetter(FindSentMapLetter findSentMapLetter) {
        String targetUserNickname = null;
        if (findSentMapLetter.getType().equals("TARGET")) {
            targetUserNickname = userUseCase.getNicknameById(findSentMapLetter.getTargetUser());
        }

        return FindMapLetterResponse.from(findSentMapLetter, targetUserNickname,
                findSentMapLetter.getType().equals("REPLY") ? REPLY : MAP);
    }

    public Page<FindReceivedMapLetterResponse> findReceivedMapLetters(int page, int size, Long userId) {
        PageValidator.validMinPage(page);

        Page<FindReceivedMapLetterDTO> letters = mapLetterUseCase.findReceivedMapLetters(page, size, userId);

        if (letters.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(page - 1, size), 0);
        }

        PageValidator.validMaxPage(letters.getTotalPages(), page);

        return letters.map(letter -> {
            String senderNickname = null;
            String senderProfileImg = null;

            if ("TARGET".equals(letter.getType())) {
                senderNickname = userUseCase.getNicknameById(letter.getSenderId());
                senderProfileImg = userUseCase.getProfileImageUrlById(letter.getSenderId());
            }

            return FindReceivedMapLetterResponse.from(letter, senderNickname, senderProfileImg, MAP);
        });
    }

    public Page<FindAllSentMapLetterResponse> findAllSentMapLetters(int page, int size, Long userId) {
        PageValidator.validMinPage(page);

        Page<MapLetter> letters = mapLetterUseCase.findActiveByCreateUserId(page, size, userId);

        if (letters.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(page - 1, size), 0);
        }

        PageValidator.validMaxPage(letters.getTotalPages(), page);

        return letters.map(mapLetter -> {
            String targetUserNickname = null;
            if (mapLetter.getType() == MapLetterType.PRIVATE) {
                targetUserNickname = userUseCase.getNicknameById(mapLetter.getTargetUserId());
            }
            return FindAllSentMapLetterResponse.from(mapLetter, targetUserNickname, MAP);
        });
    }

    public Page<FindAllReceivedLetterResponse> findAllReceivedLetters(int page, int size, Long userId) {
        PageValidator.validMinPage(page);

        Page<MapLetter> letters = mapLetterUseCase.findActiveLetter(page, size, userId);

        if (letters.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(page - 1, size), 0);
        }
        PageValidator.validMaxPage(letters.getTotalPages(), page);
        return letters.map(letter -> {
            String sendUserNickname = userUseCase.getNicknameById(letter.getCreateUserId());
            String sendUserProfileImg = userUseCase.getProfileImageUrlById(letter.getCreateUserId());
            return FindAllReceivedLetterResponse.from(letter, sendUserNickname, sendUserProfileImg,
                    MAP);
        });
    }
}
