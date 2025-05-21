package online.bottler.mapletter.application.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import online.bottler.mapletter.application.dto.request.DeleteMapLettersRequestDTO;
import online.bottler.mapletter.domain.MapLetter;
import online.bottler.mapletter.domain.MapLetterType;

@Builder
public record FindAllSentMapLetterResponseDTO(
        Long letterId,
        String title,
        String description,
        BigDecimal latitude,
        BigDecimal longitude,
        String label,
        String targetUserNickname,
        LocalDateTime createdAt,
        String type, //TARGET(타겟 편지), PUBLIC(퍼블릭 편지)
        DeleteMapLettersRequestDTO.LetterType deleteType //삭제 타입
) {
    public static FindAllSentMapLetterResponseDTO from(MapLetter mapLetter, String targetUserNickname,
                                                       DeleteMapLettersRequestDTO.LetterType deleteType) {
        return FindAllSentMapLetterResponseDTO.builder()
                .letterId(mapLetter.getId())
                .title(mapLetter.getTitle())
                .description(mapLetter.getDescription())
                .latitude(mapLetter.getLatitude())
                .longitude(mapLetter.getLongitude())
                .label(mapLetter.getLabel())
                .targetUserNickname(targetUserNickname)
                .createdAt(mapLetter.getCreatedAt())
                .type(mapLetter.getType() == MapLetterType.PRIVATE ? "TARGET" : "PUBLIC")
                .deleteType(deleteType)
                .build();
    }
}
