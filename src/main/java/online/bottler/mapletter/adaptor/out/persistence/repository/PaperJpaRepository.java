package online.bottler.mapletter.adaptor.out.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import online.bottler.mapletter.adaptor.out.persistence.entity.PaperEntity;

@Repository
public interface PaperJpaRepository extends JpaRepository<PaperEntity, Long> {
}
