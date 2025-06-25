package online.bottler.mapletter.application.port.in;

import java.util.List;
import java.util.Map;
import online.bottler.mapletter.application.dto.FindReceivedMapLetterDTO;
import online.bottler.mapletter.application.dto.FindSentMapLetter;
import online.bottler.mapletter.application.dto.MapLetterAndDistance;
import online.bottler.mapletter.application.response.FindAllReceivedLetterResponse;
import online.bottler.mapletter.application.response.FindAllSentMapLetterResponse;
import online.bottler.mapletter.application.response.FindMapLetterResponse;
import online.bottler.mapletter.application.response.FindNearbyLettersResponse;
import online.bottler.mapletter.application.response.FindReceivedMapLetterResponse;
import online.bottler.mapletter.domain.MapLetter;
import org.springframework.data.domain.Page;

public interface MapLetterConvertUseCase {
    Page<FindMapLetterResponse> convertToFindMapLetterResponse(Page<FindSentMapLetter> sentLetters,
                                                               Map<Long, String> nicknamesByIds);

    Page<FindReceivedMapLetterResponse> convertToFindReceivedMapLetterResponse(Page<FindReceivedMapLetterDTO> letters,
                                                                               Map<Long, String> nicknamesByIds,
                                                                               Map<Long, String> profileImageUrlsByIds);

    Page<FindAllSentMapLetterResponse> convertToFindAllSentMapLetterResponse(Page<MapLetter> letters,
                                                                             Map<Long, String> nicknamesByIds);

    Page<FindAllReceivedLetterResponse> convertToFindAllReceivedLetterResponse(Page<MapLetter> letters,
                                                                               Map<Long, String> nicknamesByIds,
                                                                               Map<Long, String> profileImageUrlsByIds);

    List<FindNearbyLettersResponse> convertToFindNearbyResponses(List<MapLetterAndDistance> letters,
                                                                 Map<Long, String> nicknamesByIds);
}
