package online.bottler.mapletter.application.response;

import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import online.bottler.mapletter.application.dto.MapLetterAndDistance;

@Builder
public record FindNearbyLettersResponse(
        Long letterId,
        BigDecimal latitude,
        BigDecimal longitude,
        String title,
        LocalDateTime createdAt,
        BigDecimal distance,
        Long target,
        String createUserNickname,
        String label,
        String description
) {
    public static FindNearbyLettersResponse from(MapLetterAndDistance letterWithDistance, String nickname) {
        return FindNearbyLettersResponse.builder()
                .letterId(letterWithDistance.getLetterId())
                .latitude(letterWithDistance.getLatitude())
                .longitude(letterWithDistance.getLongitude())
                .title(letterWithDistance.getTitle())
                .createdAt(letterWithDistance.getCreatedAt())
                .distance(letterWithDistance.getDistance())
                .target(letterWithDistance.getTargetUserId())
                .createUserNickname(nickname)
                .label(letterWithDistance.getLabel())
                .description(letterWithDistance.getDescription())
                .build();
    }
}
