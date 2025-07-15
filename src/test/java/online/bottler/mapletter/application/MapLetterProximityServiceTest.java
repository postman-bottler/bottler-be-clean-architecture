package online.bottler.mapletter.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import online.bottler.RedisTestContainersConfig;
import online.bottler.global.exception.CommonForbiddenException;
import online.bottler.global.exception.DomainException;
import online.bottler.mapletter.adaptor.out.persistence.entity.MapLetterEntity;
import online.bottler.mapletter.adaptor.out.persistence.repository.MapLetterJpaRepository;
import online.bottler.mapletter.application.dto.MapLetterAndDistance;
import online.bottler.mapletter.domain.MapLetterType;
import online.bottler.user.adapter.out.persistence.entity.UserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@Import(RedisTestContainersConfig.class)
@Transactional
class MapLetterProximityServiceTest extends MapLetterApplicationTestHelper {

    @Autowired
    private MapLetterProximityService mapLetterProximityService;

    @Autowired
    private MapLetterJpaRepository mapLetterJpaRepository;

    @AfterEach
    void tearDown() {
        mapLetterJpaRepository.deleteAllInBatch();
    }

    @DisplayName("반경 500M 안에 있는 편지 조회시 PRIVATE 편지의 타겟이 조회를 요청한 유저이거나 PUBLIC 편지만 조회가 가능하다.")
    @Test
    void findNearByMapLettersTest() {
        //given
        UserEntity targetUser = saveUser();
        UserEntity createUser = saveUser();
        UserEntity user = saveUser();

        BigDecimal latitude = new BigDecimal("37.5665");
        BigDecimal longitude = new BigDecimal("126.9780");

        mapLetterJpaRepository.save(createMapLetterEntity(
                createUser.getUserId(), new BigDecimal("37.567454"), new BigDecimal("126.979203"),
                MapLetterType.PUBLIC)); //150m
        mapLetterJpaRepository.save(createMapLetterEntity(
                createUser.getUserId(), new BigDecimal("37.567454"), new BigDecimal("126.979203"),
                MapLetterType.PRIVATE, targetUser.getUserId())); //150m
        mapLetterJpaRepository.save(createMapLetterEntity(
                createUser.getUserId(), new BigDecimal("37.56967"), new BigDecimal("126.98201"),
                MapLetterType.PUBLIC)); //499.99m

        mapLetterJpaRepository.save(createMapLetterEntity(
                createUser.getUserId(), new BigDecimal("37.567454"), new BigDecimal("126.979203"),
                MapLetterType.PRIVATE, user.getUserId())); //150m
        mapLetterJpaRepository.save(createMapLetterEntity(
                createUser.getUserId(), new BigDecimal("37.569686"), new BigDecimal("126.982020"),
                MapLetterType.PUBLIC)); //600m
        mapLetterJpaRepository.save(createMapLetterEntity(
                createUser.getUserId(), new BigDecimal("37.570315"), new BigDecimal("126.982814"),
                MapLetterType.PUBLIC)); //501m

        //when
        List<MapLetterAndDistance> responses = mapLetterProximityService.findLettersByUserLocation(latitude,
                longitude, targetUser.getUserId());

        //then
        assertThat(responses).hasSize(3);
    }

    @DisplayName("로그인하지 않은 유저가 편지를 반경 500M 안에 있는 편지들를 조회하면 PUBLIC인 편지들만 조회된다.")
    @Test
    void guestFindsOnlyPublicLettersWithin500mTest() {
        //given
        UserEntity savedUser = saveUser();

        BigDecimal latitude = new BigDecimal("37.5665");
        BigDecimal longitude = new BigDecimal("126.9780");

        mapLetterJpaRepository.save(createMapLetterEntity(
                savedUser.getUserId(), new BigDecimal("37.567454"), new BigDecimal("126.979203"),
                MapLetterType.PUBLIC)); //150m
        mapLetterJpaRepository.save(createMapLetterEntity(
                savedUser.getUserId(), new BigDecimal("37.56967"), new BigDecimal("126.98201"),
                MapLetterType.PUBLIC)); //499.99m

        mapLetterJpaRepository.save(createMapLetterEntity(
                savedUser.getUserId(), new BigDecimal("37.567454"), new BigDecimal("126.979203"),
                MapLetterType.PRIVATE)); //150m
        mapLetterJpaRepository.save(createMapLetterEntity(
                savedUser.getUserId(), new BigDecimal("37.569686"), new BigDecimal("126.982020"),
                MapLetterType.PUBLIC)); //600m
        mapLetterJpaRepository.save(createMapLetterEntity(
                savedUser.getUserId(), new BigDecimal("37.570315"), new BigDecimal("126.982814"),
                MapLetterType.PUBLIC)); //501m

        //when
        List<MapLetterAndDistance> responses = mapLetterProximityService.findGuestNearByMapLetters(latitude, longitude);

        //then
        assertThat(responses).hasSize(2);
    }

    @DisplayName("비로그인 유저가 반경 15M를 벗어난 편지를 조회할 경우, 예외가 발생한다.")
    @Test
    void guestCannotViewLetterOutside15mRadiusTest() {
        //given
        UserEntity user = saveUser();
        BigDecimal latitude = new BigDecimal("37.5665");
        BigDecimal longitude = new BigDecimal("126.9780");

        MapLetterEntity mapLetter = mapLetterJpaRepository.save(createMapLetterEntity(
                user.getUserId(), new BigDecimal("37.569686"), new BigDecimal("126.982020"),
                MapLetterType.PUBLIC)); //600m

        //when, then
        assertThatThrownBy(
                () -> mapLetterProximityService.validateViewDistance(latitude, longitude, mapLetter.getMapLetterId(),
                        MapLetterEntity.toDomain(mapLetter)))
                .isInstanceOf(DomainException.class)
                .hasMessage("편지와의 거리가 멀어서 조회가 불가능합니다.");
    }

    @DisplayName("비로그인 유저가 반경 15M 안에 있는 삭제된 편지를 조회할 경우, 예외가 발생한다.")
    @Test
    void guestCannotViewDeletedLetterWithin15mRadiusTest() {
        //given
        UserEntity user = saveUser();
        BigDecimal latitude = new BigDecimal("37.5665");
        BigDecimal longitude = new BigDecimal("126.9780");

        MapLetterEntity mapLetter = mapLetterJpaRepository.save(createMapLetterEntity(
                user.getUserId(), new BigDecimal("37.5666"), new BigDecimal("126.9781"),
                MapLetterType.PUBLIC, null, true, false));

        //when, then
        assertThatThrownBy(
                () -> mapLetterProximityService.validateViewDistance(latitude, longitude, mapLetter.getMapLetterId(),
                        MapLetterEntity.toDomain(mapLetter)))
                .isInstanceOf(DomainException.class)
                .hasMessage("해당 편지는 삭제되었습니다.");
    }

    @DisplayName("비로그인 유저가 반경 15M 안에 있는 블락된 편지를 조회할 경우, 예외가 발생한다.")
    @Test
    void guestCannotViewBlockedLetterWithin15mRadiusTest() {
        //given
        UserEntity user = saveUser();
        BigDecimal latitude = new BigDecimal("37.5665");
        BigDecimal longitude = new BigDecimal("126.9780");

        MapLetterEntity mapLetter = mapLetterJpaRepository.save(createMapLetterEntity(
                user.getUserId(), new BigDecimal("37.5666"), new BigDecimal("126.9781"),
                MapLetterType.PUBLIC, null, false, true));

        //when, then
        assertThatThrownBy(
                () -> mapLetterProximityService.validateViewDistance(latitude, longitude, mapLetter.getMapLetterId(),
                        MapLetterEntity.toDomain(mapLetter)))
                .isInstanceOf(DomainException.class)
                .hasMessage("해당 편지는 신고당한 편지입니다.");
    }

    @DisplayName("로그인 한 유저가 반경 15M를 벗어난 편지를 조회할 경우, 예외가 발생한다.")
    @Test
    void userCannotViewLetterOutside15mRadiusTest() {
        //given
        UserEntity user = saveUser();
        BigDecimal latitude = new BigDecimal("37.5665");
        BigDecimal longitude = new BigDecimal("126.9780");

        MapLetterEntity mapLetter = mapLetterJpaRepository.save(createMapLetterEntity(
                user.getUserId(), new BigDecimal("37.569686"), new BigDecimal("126.982020"),
                MapLetterType.PUBLIC)); //600m

        //when, then
        assertThatThrownBy(
                () -> mapLetterProximityService.validateMapLetterViewPermission(latitude, longitude,
                        mapLetter.getMapLetterId(), MapLetterEntity.toDomain(mapLetter), user.getUserId()))
                .isInstanceOf(DomainException.class)
                .hasMessage("편지와의 거리가 멀어서 조회가 불가능합니다.");
    }

    @DisplayName("로그인 한 유저가 작성하지 않거나 타겟이 아닌 PRIVATE 편지를 조회 할 경우 예외가 발생한다.")
    @Test
    void unauthorizedUserCannotAccessPrivateLetterTest() {
        //given
        UserEntity createUser = saveUser();
        UserEntity targetUser = saveUser();
        UserEntity user = saveUser();
        BigDecimal latitude = new BigDecimal("37.5665");
        BigDecimal longitude = new BigDecimal("126.9780");

        MapLetterEntity mapLetter = mapLetterJpaRepository.save(createMapLetterEntity(
                createUser.getUserId(), new BigDecimal("37.5666"), new BigDecimal("126.9781"),
                MapLetterType.PRIVATE, targetUser.getUserId())); //14.8m

        //when, then
        assertThatThrownBy(
                () -> mapLetterProximityService.validateMapLetterViewPermission(latitude, longitude,
                        mapLetter.getMapLetterId(), MapLetterEntity.toDomain(mapLetter), user.getUserId()))
                .isInstanceOf(CommonForbiddenException.class)
                .hasMessage("편지를 볼 수 있는 권한이 없습니다.");
    }

    @DisplayName("로그인 유저가 반경 15M 안에 있는 삭제된 편지를 조회할 경우, 예외가 발생한다.")
    @Test
    void userCannotViewDeletedLetterWithin15mRadiusTest() {
        //given
        UserEntity user = saveUser();
        BigDecimal latitude = new BigDecimal("37.5665");
        BigDecimal longitude = new BigDecimal("126.9780");

        MapLetterEntity mapLetter = mapLetterJpaRepository.save(createMapLetterEntity(
                user.getUserId(), new BigDecimal("37.5666"), new BigDecimal("126.9781"),
                MapLetterType.PUBLIC, null, true, false));

        //when, then
        assertThatThrownBy(
                () -> mapLetterProximityService.validateMapLetterViewPermission(latitude, longitude,
                        mapLetter.getMapLetterId(),
                        MapLetterEntity.toDomain(mapLetter), user.getUserId()))
                .isInstanceOf(DomainException.class)
                .hasMessage("해당 편지는 삭제되었습니다.");
    }

    @DisplayName("로그인 유저가 반경 15M 안에 있는 블락된 편지를 조회할 경우, 예외가 발생한다.")
    @Test
    void userCannotViewBlockedLetterWithin15mRadiusTest() {
        //given
        UserEntity user = saveUser();
        BigDecimal latitude = new BigDecimal("37.5665");
        BigDecimal longitude = new BigDecimal("126.9780");

        MapLetterEntity mapLetter = mapLetterJpaRepository.save(createMapLetterEntity(
                user.getUserId(), new BigDecimal("37.5666"), new BigDecimal("126.9781"),
                MapLetterType.PUBLIC, null, false, true));

        //when, then
        assertThatThrownBy(
                () -> mapLetterProximityService.validateMapLetterViewPermission(latitude, longitude,
                        mapLetter.getMapLetterId(),
                        MapLetterEntity.toDomain(mapLetter), user.getUserId()))
                .isInstanceOf(DomainException.class)
                .hasMessage("해당 편지는 신고당한 편지입니다.");
    }
}
