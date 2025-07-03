package online.bottler.letter.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import online.bottler.letter.domain.Letter;
import online.bottler.letter.domain.LetterContent;
import online.bottler.letter.domain.LetterStatus;

@Entity
@Table(name = "letters",
        indexes = @Index(name = "idx_letter_status_id", columnList = ("status, id")))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LetterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String content;
    private String font;
    private String paper;
    private String label;
    private Long userId;
    private LetterStatus status;
    private LocalDateTime createdAt;

    @Builder
    private LetterEntity(String title, String content, String font, String paper, String label, Long userId,
                         LetterStatus status,
                         LocalDateTime createdAt) {
        this.title = title;
        this.content = content;
        this.font = font;
        this.paper = paper;
        this.label = label;
        this.userId = userId;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static LetterEntity from(Letter letter) {
        return LetterEntity.builder()
                .title(letter.getTitle())
                .content(letter.getContent())
                .font(letter.getFont())
                .paper(letter.getPaper())
                .label(letter.getLabel())
                .userId(letter.getUserId())
                .status(letter.getStatus())
                .createdAt(letter.getCreatedAt())
                .build();
    }

    public Letter toDomain() {
        return Letter.of(id, userId, LetterContent.of(title, content, font, paper, label),
                status, createdAt);
    }

    public static List<Letter> toDomainList(List<LetterEntity> letterEntities) {
        return letterEntities.stream().map(LetterEntity::toDomain).toList();
    }
}
