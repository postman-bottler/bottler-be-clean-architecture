package online.bottler.letter.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import online.bottler.letter.domain.LetterContent;
import online.bottler.letter.domain.ReplyLetter;

@Entity
@Table(
        name = "reply_letters",
        indexes = @Index(name = "idx_replyletter_isdeleted_id", columnList = ("isDeleted, id")),
        uniqueConstraints = @UniqueConstraint(name = "uq_letter_sender", columnNames = {"letterId", "senderId"})
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReplyLetterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    private String font;
    private String paper;
    private String label;
    private Long letterId;
    private Long receiverId;
    private Long senderId;
    private boolean isDeleted;
    private boolean isBlocked;
    private LocalDateTime createdAt;

    @Builder
    private ReplyLetterEntity(String title, String content, String font, String paper, String label,
                              Long letterId, Long receiverId, Long senderId,
                              boolean isDeleted, boolean isBlocked,
                              LocalDateTime createdAt) {
        this.title = title;
        this.content = content;
        this.font = font;
        this.paper = paper;
        this.label = label;
        this.letterId = letterId;
        this.receiverId = receiverId;
        this.senderId = senderId;
        this.isDeleted = isDeleted;
        this.isBlocked = isBlocked;
        this.createdAt = createdAt;
    }

    public static ReplyLetterEntity from(ReplyLetter replyLetter) {
        return ReplyLetterEntity.builder()
                .title(replyLetter.getTitle())
                .content(replyLetter.getContent())
                .font(replyLetter.getFont())
                .paper(replyLetter.getPaper())
                .label(replyLetter.getLabel())
                .letterId(replyLetter.getLetterId())
                .receiverId(replyLetter.getReceiverId())
                .senderId(replyLetter.getUserId())
                .isDeleted(replyLetter.isDeleted())
                .isBlocked(replyLetter.isBlocked())
                .createdAt(replyLetter.getCreatedAt())
                .build();
    }

    public ReplyLetter toDomain() {
        return ReplyLetter.of(
                id,
                senderId,
                LetterContent.of(
                        title,
                        content,
                        font,
                        paper,
                        label
                ),
                isDeleted,
                isBlocked,
                letterId,
                receiverId,
                createdAt
        );
    }
}
