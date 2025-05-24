package online.bottler.label.adapter.out.persistence.repository;

import java.util.List;
import online.bottler.label.adapter.out.persistence.entity.LabelEntity;
import online.bottler.label.adapter.out.persistence.entity.UserLabelEntity;
import online.bottler.label.domain.LabelType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserLabelJpaRepository extends JpaRepository<UserLabelEntity, Long> {
    @Query("SELECT ul.label FROM UserLabelEntity ul WHERE ul.user.userId = :userId")
    List<LabelEntity> findLabelsByUserId(@Param("userId") Long userId);

    @Query("SELECT l FROM LabelEntity l WHERE l.labelType = :labelType")
    List<LabelEntity> findFirstComeLabels(@Param("labelType") LabelType labelType);

    boolean existsByUserUserIdAndLabelLabelId(Long userId, Long labelId);
}
