package online.bottler.mapletter.application.port.in;

import java.util.List;
import java.util.Set;
import online.bottler.mapletter.application.dto.FindReceivedMapLetterDTO;
import online.bottler.mapletter.application.dto.FindSentMapLetter;
import online.bottler.mapletter.application.dto.MapLetterAndDistance;
import online.bottler.mapletter.domain.MapLetter;
import org.springframework.data.domain.Page;

public interface MapLetterExtractUseCase {
    Set<Long> extractTargetUserIdsBySentMapLetters(Page<FindSentMapLetter> sentLetters);

    Set<Long> extractSentUserIdsByReceivedMapLetters(Page<FindReceivedMapLetterDTO> letters);

    Set<Long> extractTargetUserIdsByMapLetters(Page<MapLetter> letters);

    Set<Long> extractCreateUserIdsByMapLetters(Page<MapLetter> letters);

    Set<Long> extractCreateUserIds(List<MapLetterAndDistance> letters);
}
