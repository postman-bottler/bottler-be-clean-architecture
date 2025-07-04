package online.bottler.letter.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import online.bottler.letter.domain.LetterStatus;
import online.bottler.letter.domain.ReplyLetter;

@Entity
@Table(
        name = "reply_letters",
        indexes = {
                @Index(name = "idx_replyletter_receiverId_letterId_isDeleted", columnList = "receiverId, letterId, status"),
                @Index(name = "idx_senderId_status", columnList = "senderId, status")
        },
        uniqueConstraints = @UniqueConstraint(name = "uq_letter_sender", columnNames = {"senderId", "letterId"})
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReplyLetterEntity extends BaseLetterEntity {

    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    @Column(name = "receiver_id", nullable = false)
    private Long receiverId;

    @Column(name = "letter_id", nullable = false)
    private Long letterId;

    @Builder
    private ReplyLetterEntity(
            Long id,
            Long senderId, Long receiverId,
            Long letterId,
            LetterContentEntity letterContentEntity,
            LetterStatus status
    ) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.letterId = letterId;
        this.letterContentEntity = letterContentEntity;
        this.status = status;
    }

    public static ReplyLetterEntity from(ReplyLetter replyLetter) {
        return ReplyLetterEntity.builder()
                .id(replyLetter.getId())
                .senderId(replyLetter.getSenderId())
                .receiverId(replyLetter.getReceiverId())
                .letterId(replyLetter.getLetterId())
                .letterContentEntity(LetterContentEntity.from(replyLetter.getLetterContent()))
                .status(replyLetter.getStatus())
                .build();
    }

    public static Iterable<ReplyLetterEntity> fromList(List<ReplyLetter> replyLetters) {
        return replyLetters.stream().map(ReplyLetterEntity::from).toList();
    }

    public ReplyLetter toDomain() {
        return ReplyLetter.of(
                id,
                senderId, receiverId,
                letterId,
                letterContentEntity.toDomain(),
                status,
                createdAt
        );
    }

    public static List<ReplyLetter> toDomainList(List<ReplyLetterEntity> replyLetterEntities) {
        return replyLetterEntities.stream().map(ReplyLetterEntity::toDomain).toList();
    }
}
