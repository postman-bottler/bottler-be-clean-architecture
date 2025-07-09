package online.bottler.letter.domain;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ReplyLetter extends BaseLetter {

    private final Long senderId;

    private final Long receiverId;

    private final Long letterId;

    private ReplyLetter(
            Long id,
            Long senderId, Long receiverId,
            Long letterId,
            LetterContent letterContent,
            LetterStatus status,
            LocalDateTime createdAt
    ) {
        super(id, letterContent, status, createdAt);
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.letterId = letterId;
    }

    public static ReplyLetter of(
            Long id,
            Long senderId, Long receiverId,
            Long letterId,
            LetterContent letterContent,
            LetterStatus status,
            LocalDateTime createdAt
    ) {
        return new ReplyLetter(id, senderId, receiverId, letterId, letterContent, status, createdAt);
    }

    public static ReplyLetter create(
            Long senderId, Long receiverId,
            Long letterId,
            LetterContent letterContent,
            String originalTitle
    ) {
        String formattedTitle = formatReplyTitle(originalTitle);

        return new ReplyLetter(
                null,
                senderId, receiverId,
                letterId,
                LetterContent.of(
                        formattedTitle,
                        letterContent.content(),
                        letterContent.font(),
                        letterContent.paper(),
                        letterContent.label()
                ),
                LetterStatus.OPEN,
                null
        );
    }

    private static String formatReplyTitle(String title) {
        return "RE: [" + title + "]";
    }

    public boolean isOwner(Long userId) {
        return senderId.equals(userId);
    }
}
