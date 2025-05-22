package online.bottler.mapletter.application.response;

import java.time.LocalDateTime;

import lombok.Builder;
import online.bottler.mapletter.application.DeleteLetterType;
import online.bottler.mapletter.application.dto.FindSentMapLetter;

@Builder
public record FindMapLetterResponse(
        Long letterId,
        String title, // 답장 편지의 경우 Re: 답장 편지 이름
        String description, // 답장 편지의 경우 생략
        String label,
        String targetUserNickname, // 타겟 편지의 경우만
        LocalDateTime createdAt,
        String type, //REPLY(답장 편지), TARGET(타겟 편지), PUBLIC(퍼블릭 편지)
        Long sourceLetterId, //답장 편지의 경우 원본 편지 id
        DeleteLetterType deleteType //삭제 타입
) {
    public static FindMapLetterResponse from(FindSentMapLetter projection, String targetUserNickname,
                                             DeleteLetterType deleteType) {
        return FindMapLetterResponse.builder()
                .letterId(projection.getLetterId())
                .title(projection.getTitle())
                .description(projection.getDescription())
                .label(projection.getLabel())
                .targetUserNickname(targetUserNickname)
                .createdAt(projection.getCreatedAt())
                .type(projection.getType())
                .sourceLetterId(projection.getSourceLetterId())
                .deleteType(deleteType)
                .build();
    }
}
