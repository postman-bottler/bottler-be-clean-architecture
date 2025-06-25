package online.bottler.user.adapter.out.persistence.repository;

import io.lettuce.core.dynamic.annotation.Param;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import online.bottler.user.adapter.out.persistence.entity.UserEntity;
import online.bottler.user.domain.Provider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END " +
            "FROM UserEntity u WHERE u.email = :email AND u.isDeleted = false")
    boolean existsByEmail(String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END " +
            "FROM UserEntity u WHERE u.nickname = :nickname AND u.isDeleted = false")
    boolean existsByNickname(String nickname);

    Optional<UserEntity> findByEmail(String email);

    List<UserEntity> findByUserIdIn(List<Long> ids);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END " +
            "FROM UserEntity u WHERE u.email = :kakaoId AND u.provider = :provider AND u.isDeleted = false")
    boolean existsByEmailAndProvider(String kakaoId, Provider provider);

    Optional<UserEntity> findByNickname(String nickname);

    @Query("SELECT userId, nickname FROM UserEntity WHERE userId IN :ids")
    List<Object[]> getIdAndNicknameByUserIdIn(Set<Long> ids);

    @Query("SELECT userId, imageUrl FROM UserEntity WHERE userId IN :ids")
    List<Object[]> getIdAndImageByUserIdIn(Set<Long> ids);
}
