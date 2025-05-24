package online.bottler.mapletter.application.response;

import lombok.Builder;
import java.time.LocalDateTime;
import online.bottler.mapletter.domain.MapLetter;

@Builder
public record OneLetterResponse(
        String title,
        String content,
        String description,
        String profileImg,
        String font,
        String paper,
        String label,
        LocalDateTime createdAt,
        boolean isOwner,
        boolean isReplied,
        boolean isArchived,
        boolean isTarget
) {
    public static OneLetterResponse from(MapLetter mapLetter, String profileImg, boolean isOwner, boolean isReplied,
                                         boolean isArchived) {
        return OneLetterResponse.builder()
                .title(mapLetter.getTitle())
                .content(mapLetter.getContent())
                .font(mapLetter.getFont())
                .paper(mapLetter.getPaper())
                .label(mapLetter.getLabel())
                .profileImg(profileImg)
                .createdAt(mapLetter.getCreatedAt())
                .description(mapLetter.getDescription())
                .isOwner(isOwner)
                .isReplied(isReplied)
                .isArchived(isArchived)
                .isTarget(mapLetter.getTargetUserId() != null)
                .build();
    }
}
