package online.bottler.user.adapter.out.persistence.repository;

import online.bottler.user.adapter.out.persistence.entity.ProfileImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProfileImageJpaRepository extends JpaRepository<ProfileImageEntity, Long> {
    boolean existsByImageUrl(String newProfileImage);

    @Query("SELECT p.imageUrl FROM ProfileImageEntity p ORDER BY FUNCTION('RAND') LIMIT 1")
    String findRandomProfileImage();
}
