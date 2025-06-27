package online.bottler.mapletter.application;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import online.bottler.mapletter.application.dto.FindReceivedMapLetterDTO;
import online.bottler.mapletter.application.dto.FindSentMapLetter;
import online.bottler.mapletter.application.dto.MapLetterAndDistance;
import online.bottler.mapletter.application.port.in.MapLetterExtractUseCase;
import online.bottler.mapletter.domain.MapLetter;
import online.bottler.mapletter.domain.MapLetterType;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class MapLetterExtractService implements MapLetterExtractUseCase {

    @Override
    public Set<Long> extractTargetUserIdsBySentMapLetters(Page<FindSentMapLetter> sentLetters) {
        return sentLetters.getContent().stream()
                .filter(letter -> "TARGET".equals(letter.getType()))
                .map(FindSentMapLetter::getTargetUser)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Long> extractSentUserIdsByReceivedMapLetters(Page<FindReceivedMapLetterDTO> letters) {
        return letters.getContent().stream()
                .filter(letter -> "TARGET".equals(letter.getType()))
                .map(FindReceivedMapLetterDTO::getSenderId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Long> extractTargetUserIdsByMapLetters(Page<MapLetter> letters) {
        return letters.getContent().stream()
                .filter(letter -> MapLetterType.PRIVATE.equals(letter.getType()))
                .map(MapLetter::getTargetUserId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Long> extractCreateUserIdsByMapLetters(Page<MapLetter> letters) {
        return letters.getContent().stream()
                .map(MapLetter::getCreateUserId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Long> extractCreateUserIds(List<MapLetterAndDistance> letters) {
        return letters.stream()
                .map(MapLetterAndDistance::getCreateUserId)
                .collect(Collectors.toSet());
    }
}
