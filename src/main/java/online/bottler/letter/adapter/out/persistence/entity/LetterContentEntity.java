package online.bottler.letter.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import online.bottler.letter.domain.LetterContent;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LetterContentEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "font", nullable = false)
    private String font;

    @Column(name = "paper", nullable = false)
    private String paper;

    @Column(name = "label", nullable = false)
    private String label;

    @Builder
    private LetterContentEntity(String title, String content, String font, String paper, String label) {
        this.title = title;
        this.content = content;
        this.font = font;
        this.paper = paper;
        this.label = label;
    }

    public static LetterContentEntity from(LetterContent letterContent) {
        return LetterContentEntity.builder()
                .title(letterContent.getTitle())
                .content(letterContent.getContent())
                .font(letterContent.getFont())
                .paper(letterContent.getPaper())
                .label(letterContent.getLabel())
                .build();
    }

    public LetterContent toDomain() {
        return LetterContent.of(title, content, font, paper, label);
    }
}
