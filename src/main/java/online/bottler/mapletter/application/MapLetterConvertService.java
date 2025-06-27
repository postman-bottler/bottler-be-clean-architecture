package online.bottler.mapletter.application;

import static online.bottler.mapletter.application.DeleteLetterType.MAP;
import static online.bottler.mapletter.application.DeleteLetterType.REPLY;

import java.util.List;
import java.util.Map;
import online.bottler.mapletter.application.dto.FindReceivedMapLetterDTO;
import online.bottler.mapletter.application.dto.FindSentMapLetter;
import online.bottler.mapletter.application.dto.MapLetterAndDistance;
import online.bottler.mapletter.application.port.in.MapLetterConvertUseCase;
import online.bottler.mapletter.application.response.FindAllReceivedLetterResponse;
import online.bottler.mapletter.application.response.FindAllSentMapLetterResponse;
import online.bottler.mapletter.application.response.FindMapLetterResponse;
import online.bottler.mapletter.application.response.FindNearbyLettersResponse;
import online.bottler.mapletter.application.response.FindReceivedMapLetterResponse;
import online.bottler.mapletter.domain.MapLetter;
import online.bottler.mapletter.domain.MapLetterType;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class MapLetterConvertService implements MapLetterConvertUseCase {

    @Override
    public Page<FindAllReceivedLetterResponse> convertToFindAllReceivedLetterResponse(Page<MapLetter> letters,
                                                                                      Map<Long, String> nicknamesByIds,
                                                                                      Map<Long, String> profileImageUrlsByIds) {
        return letters.map(letter -> {
            String sendUserNickname = nicknamesByIds.get(letter.getCreateUserId());
            String sendUserProfileImg = profileImageUrlsByIds.get(letter.getCreateUserId());
            return FindAllReceivedLetterResponse.from(letter, sendUserNickname, sendUserProfileImg, MAP);
        });
    }

    @Override
    public Page<FindAllSentMapLetterResponse> convertToFindAllSentMapLetterResponse(Page<MapLetter> letters,
                                                                                    Map<Long, String> nicknamesByIds) {
        return letters.map(mapLetter -> {
            String targetUserNickname = null;
            if (mapLetter.getType() == MapLetterType.PRIVATE) {
                targetUserNickname = nicknamesByIds.get(mapLetter.getTargetUserId());
            }
            return FindAllSentMapLetterResponse.from(mapLetter, targetUserNickname, MAP);
        });
    }

    @Override
    public Page<FindReceivedMapLetterResponse> convertToFindReceivedMapLetterResponse(
            Page<FindReceivedMapLetterDTO> letters, Map<Long, String> nicknamesByIds,
            Map<Long, String> profileImageUrlsByIds) {

        return letters.map(letter -> {
            String senderNickname = null;
            String senderProfileImageUrl = null;

            if ("TARGET".equals(letter.getType())) {
                senderNickname = nicknamesByIds.get(letter.getSenderId());
                senderProfileImageUrl = profileImageUrlsByIds.get(letter.getSenderId());
            }

            return FindReceivedMapLetterResponse.from(letter, senderNickname, senderProfileImageUrl, MAP);
        });
    }

    @Override
    public Page<FindMapLetterResponse> convertToFindMapLetterResponse(Page<FindSentMapLetter> sentLetters,
                                                                      Map<Long, String> nicknamesByIds) {
        return sentLetters.map(findSentMapLetter -> {
            String targetUserNickname = null;
            if ("TARGET".equals(findSentMapLetter.getType())) {
                targetUserNickname = nicknamesByIds.get(findSentMapLetter.getTargetUser());
            }

            return FindMapLetterResponse.from(
                    findSentMapLetter,
                    targetUserNickname,
                    "REPLY".equals(findSentMapLetter.getType()) ? REPLY : MAP
            );
        });
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
}
