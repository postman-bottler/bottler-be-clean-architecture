package online.bottler.label.adapter.out.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import online.bottler.label.domain.Label;
import online.bottler.label.domain.UserLabel;
import online.bottler.user.domain.User;
import online.bottler.user.infra.entity.UserEntity;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_label")
public class UserLabelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userLabelId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @Cascade(CascadeType.PERSIST)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "label_id", nullable = false)
    @Cascade(CascadeType.PERSIST)
    private LabelEntity label;

    public static UserLabelEntity from(UserEntity userEntity, LabelEntity labelEntity) {
        return UserLabelEntity.builder()
                .user(userEntity)
                .label(labelEntity)
                .build();
    }

    public static List<UserLabel> toUserLabels(List<UserLabelEntity> entities) {
        return entities.stream()
                .map(UserLabelEntity::to)
                .toList();
    }

    public UserLabel to() {
        User user = UserEntity.toUser(this.user);
        Label label = LabelEntity.toLabel(this.label);
        return UserLabel.createUserLabel(user, label);
    }
}
