package online.bottler.mapletter.application.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import online.bottler.mapletter.application.DeleteLetterType;
import online.bottler.mapletter.application.dto.FindReceivedMapLetterDTO;

@Builder
public record FindReceivedMapLetterResponse(
        Long letterId,
        String title,
        String description,
        BigDecimal latitude,
        BigDecimal longitude,
        String label,
        LocalDateTime createdAt,
        String type, //reply, target
        Long sourceLetterId,
        String senderNickname, //타겟편지에서 누가 나한테 보냈는지
        String senderProfileImg, //타겟편지에서 보낸 사람의 프로필 이미지
        DeleteLetterType deleteType, //삭제 타입
        boolean isRead //한 번이라도 읽었는지
) {
    public static FindReceivedMapLetterResponse from(FindReceivedMapLetterDTO letterDTO, String senderNickname,
                                                     String senderProfileImg,
                                                     DeleteLetterType deleteType) {
        return FindReceivedMapLetterResponse.builder()
                .letterId(letterDTO.getLetterId())
                .title(letterDTO.getTitle())
                .description(letterDTO.getDescription())
                .latitude(letterDTO.getLatitude())
                .longitude(letterDTO.getLongitude())
                .label(letterDTO.getLabel())
                .createdAt(letterDTO.getCreatedAt())
                .type(letterDTO.getType())
                .sourceLetterId(letterDTO.getSourceLetterId())
                .senderNickname(senderNickname)
                .senderProfileImg(senderProfileImg)
                .deleteType(deleteType)
                .isRead(letterDTO.getIsRead() == 1)
                .build();
    }
}
