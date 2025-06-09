package online.bottler.letter.domain;


import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ReplyLetter {
    private final Letter letter;
    @Getter
    private final Long letterId;
    @Getter
    private final Long receiverId;

    public ReplyLetter(Letter letter, Long letterId, Long receiverId) {
        this.letter = letter;
        this.letterId = letterId;
        this.receiverId = receiverId;
    }

    public static ReplyLetter of(Long id, Long userId, LetterContent letterContent, LetterStatus letterStatus,
                                   LocalDateTime createdAt, Long letterId, Long receiverId) {
        Letter letter = Letter.of(id, userId, letterContent, letterStatus, createdAt);
        return new ReplyLetter(letter, letterId, receiverId);
    }

    public static ReplyLetter create(Long userId, LetterContent letterContent, Long letterId, Long receiverId, String originalTitle) {
        String formattedTitle = formatReplyTitle(originalTitle);

        Letter letter = Letter.create(userId, LetterContent.of(formattedTitle, letterContent.getContent(),
                letterContent.getFont(), letterContent.getPaper(), letterContent.getLabel()));

        return new ReplyLetter(letter, letterId, receiverId);
    }

    private static String formatReplyTitle(String title) {
        return "RE: [" + title + "]";
    }

    public Long getId() {
        return letter.getId();
    }

    public LocalDateTime getCreatedAt() {
        return letter.getCreatedAt();
    }

    public Long getUserId() {
        return letter.getUserId();
    }

    public String getTitle() {
        return letter.getTitle();
    }

    public String getContent() {
        return letter.getContent();
    }

    public String getFont() {
        return letter.getFont();
    }

    public String getPaper() {
        return letter.getPaper();
    }

    public String getLabel() {
        return letter.getLabel();
    }

    public boolean isDeleted() {
        return letter.isDeleted();
    }

    public boolean isBlocked() {
        return letter.isBlocked();
    }
}
