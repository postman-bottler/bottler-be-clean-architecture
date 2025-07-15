package online.bottler.mapletter.application;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import online.bottler.RedisTestContainersConfig;
import online.bottler.mapletter.adaptor.out.persistence.entity.MapLetterEntity;
import online.bottler.mapletter.domain.MapLetterType;
import online.bottler.user.adapter.out.persistence.entity.UserEntity;
import online.bottler.user.adapter.out.persistence.repository.UserJpaRepository;
import online.bottler.user.domain.Provider;
import online.bottler.user.domain.Role;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Import(RedisTestContainersConfig.class)
@Transactional
public class MapLetterApplicationTestHelper {

    private static final BigDecimal DEFAULT_LAT = new BigDecimal("37.5665");
    private static final BigDecimal DEFAULT_LNG = new BigDecimal("126.9780");

    @Autowired
    public UserJpaRepository userJpaRepository;

    @AfterEach
    void tearDown() {
        userJpaRepository.deleteAllInBatch();
    }

    public UserEntity saveUser() {
        double random = Math.random();
        return userJpaRepository.save(
                UserEntity.builder()
                        .email("target@example.com" + random)
                        .nickname("targetUserName" + random)
                        .password("pw" + random)
                        .imageUrl("http://example.com/img.png" + random)
                        .role(Role.USER)
                        .provider(Provider.LOCAL)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .isDeleted(false)
                        .warningCount(0)
                        .build()
        );
    }

    public MapLetterEntity createMapLetterEntity(Long createUserId, BigDecimal latitude, BigDecimal longitude,
                                                 MapLetterType mapLetterType, Long targetUserId, boolean isDeleted,
                                                 boolean isBlocked) {
        double random = Math.random();
        return MapLetterEntity.builder()
                .title("title" + random)
                .content("content" + random)
                .latitude(latitude)
                .longitude(longitude)
                .font("font")
                .paper("paper")
                .label("label")
                .description("description")
                .type(mapLetterType)
                .targetUserId(targetUserId)
                .createUserId(createUserId)
                .createdAt(LocalDateTime.of(2025, 7, 11, 17, 20))
                .updatedAt(LocalDateTime.of(2025, 7, 11, 17, 20))
                .isDeleted(isDeleted)
                .isBlocked(isBlocked)
                .isRead(false)
                .isRecipientDeleted(false)
                .build();
    }

    public MapLetterEntity createMapLetterEntity(Long createUserId, BigDecimal lat, BigDecimal lng, MapLetterType type,
                                                 Long targetUserId) {
        return createMapLetterEntity(createUserId, lat, lng, type, targetUserId, false, false);
    }

    public MapLetterEntity createMapLetterEntity(Long createUserId, BigDecimal lat, BigDecimal lng,
                                                 MapLetterType type) {
        return createMapLetterEntity(createUserId, lat, lng, type, null, false, false);
    }

    public MapLetterEntity createMapLetterEntity(Long createUserId, boolean isDeleted, boolean isBlocked) {
        return createMapLetterEntity(createUserId, DEFAULT_LAT, DEFAULT_LNG, MapLetterType.PUBLIC, null, isDeleted,
                isBlocked);
    }

    public MapLetterEntity createMapLetterEntity(Long createUserId, MapLetterType type) {
        return createMapLetterEntity(createUserId, DEFAULT_LAT, DEFAULT_LNG, type);
    }

    public MapLetterEntity createMapLetterEntity(Long createUserId) {
        return createMapLetterEntity(createUserId, MapLetterType.PUBLIC);
    }

    public MapLetterEntity createMapLetterEntity() {
        return createMapLetterEntity(1L);
    }

    public MapLetterEntity createMapLetterEntity(Long createUserId, Long targetUserId, boolean isDeleted,
                                                 boolean isBlocked) {
        return createMapLetterEntity(createUserId, DEFAULT_LAT, DEFAULT_LNG, MapLetterType.PRIVATE, targetUserId,
                isDeleted, isBlocked);
    }

    public MapLetterEntity createMapLetterEntity(Long createUserId, Long targetUserId) {
        return createMapLetterEntity(createUserId, targetUserId, false, false);
    }
}
