package online.bottler.label.adapter.out.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import online.bottler.label.domain.Label;
import online.bottler.label.domain.LabelType;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "label")
public class LabelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long labelId;

    @Column(unique = true, nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private int limitCount;

    @Column(nullable = false)
    @Builder.Default
    private int ownedCount = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LabelType labelType;

    public static LabelEntity from(Label label) {
        return LabelEntity.builder()
                .labelId(label.getLabelId())
                .imageUrl(label.getImageUrl())
                .limitCount(label.getLimitCount())
                .labelType(label.getLabelType())
                .build();
    }

    public static List<Label> toLabels(List<LabelEntity> entities) {
        return entities.stream()
                .map(LabelEntity::to)
                .toList();
    }

    public static Label toLabel(LabelEntity labelEntity) {
        return labelEntity.to();
    }

    public Label to() {
        return Label.createLabel(this.labelId, this.imageUrl, this.limitCount, this.ownedCount, this.labelType);
    }

    public void updateOwnedCount() {
        this.ownedCount++;
    }
}
