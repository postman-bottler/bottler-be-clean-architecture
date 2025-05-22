package online.bottler.mapletter.application.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import online.bottler.mapletter.application.DeleteLetterType;
import online.bottler.mapletter.domain.MapLetter;

@Builder
public record FindAllReceivedLetterResponse(
        Long letterId,
        String title,
        String description,
        BigDecimal latitude,
        BigDecimal longitude,
        String label,
        String sendUserNickname,
        String sendUserProfileImg,
        LocalDateTime createdAt,
        DeleteLetterType deleteType, //삭제 타입
        boolean isRead //한 번이라도 읽었는지
) {
    public static FindAllReceivedLetterResponse from(MapLetter mapLetter, String sendUserNickname,
                                                     String sendUserProfileImg, DeleteLetterType deleteType) {
        return FindAllReceivedLetterResponse.builder()
                .letterId(mapLetter.getId())
                .title(mapLetter.getTitle())
                .description(mapLetter.getDescription())
                .latitude(mapLetter.getLatitude())
                .longitude(mapLetter.getLongitude())
                .label(mapLetter.getLabel())
                .sendUserNickname(sendUserNickname)
                .sendUserProfileImg(sendUserProfileImg)
                .createdAt(mapLetter.getCreatedAt())
                .deleteType(deleteType)
                .isRead(mapLetter.isRead())
                .build();
    }
}
