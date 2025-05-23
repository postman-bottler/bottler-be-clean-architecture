package online.bottler.mapletter.adaptor.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import online.bottler.mapletter.domain.Paper;

@Entity
@Table(name = "paper")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PaperEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long paperId;

    String paperUrl;

    @Builder
    public PaperEntity(String paperUrl) {
        this.paperUrl = paperUrl;
    }

    public static PaperEntity from(Paper paper) {
        return PaperEntity.builder()
                .paperUrl(paper.getPaperUrl())
                .build();
    }

    public static Paper toDomain(PaperEntity paperEntity) {
        return Paper.builder()
                .paperId(paperEntity.paperId)
                .paperUrl(paperEntity.paperUrl)
                .build();
    }
}
