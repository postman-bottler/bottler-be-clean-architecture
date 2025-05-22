package online.bottler.mapletter.application.response;

import java.time.LocalDateTime;
import lombok.Builder;
import online.bottler.mapletter.adaptor.in.web.request.DeleteMapLettersRequest;
import online.bottler.mapletter.application.DeleteLetterType;
import online.bottler.mapletter.domain.ReplyMapLetter;

@Builder
public record FindAllReceivedReplyLetterResponse(
        Long letterId,
        String title,
        String label,
        LocalDateTime createdAt,
        Long sourceLetterId,
        DeleteLetterType deleteType //삭제 타입
) {
    public static FindAllReceivedReplyLetterResponse from(ReplyMapLetter letterDTO, String title,
                                                          DeleteLetterType deleteType) {
        return FindAllReceivedReplyLetterResponse.builder()
                .letterId(letterDTO.getReplyLetterId())
                .title(title)
                .label(letterDTO.getLabel())
                .createdAt(letterDTO.getCreatedAt())
                .sourceLetterId(letterDTO.getSourceLetterId())
                .deleteType(deleteType)
                .build();
    }
}
