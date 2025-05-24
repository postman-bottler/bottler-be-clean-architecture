package online.bottler.mapletter.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import online.bottler.mapletter.application.response.FindAllArchiveLettersResponse;

public record FindAllArchiveLettersDTO(
        Long archiveId,
        Long letterId,
        String title,
        String description,
        BigDecimal latitude,
        BigDecimal longitude,
        String label,
        LocalDateTime createdAt,
        LocalDateTime letterCreatedAt
) {
    public static FindAllArchiveLettersResponse toFindAllArchiveLettersResponse(FindAllArchiveLettersDTO dto) {
        return new FindAllArchiveLettersResponse(dto.archiveId, dto.letterId(), dto.title(), dto.description(),
                dto.latitude(), dto.longitude(), dto.label(), dto.createdAt(), dto.letterCreatedAt());
    }
}
