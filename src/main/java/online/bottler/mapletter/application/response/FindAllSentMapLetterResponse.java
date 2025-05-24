package online.bottler.mapletter.application.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import online.bottler.mapletter.application.DeleteLetterType;
import online.bottler.mapletter.domain.MapLetter;
import online.bottler.mapletter.domain.MapLetterType;

@Builder
public record FindAllSentMapLetterResponse(
        Long letterId,
        String title,
        String description,
        BigDecimal latitude,
        BigDecimal longitude,
        String label,
        String targetUserNickname,
        LocalDateTime createdAt,
        String type, //TARGET(타겟 편지), PUBLIC(퍼블릭 편지)
        DeleteLetterType deleteType //삭제 타입
) {
    public static FindAllSentMapLetterResponse from(MapLetter mapLetter, String targetUserNickname,
                                                    DeleteLetterType deleteType) {
        return FindAllSentMapLetterResponse.builder()
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
