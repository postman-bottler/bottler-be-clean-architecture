package online.bottler.mapletter.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import online.bottler.RedisTestContainersConfig;
import online.bottler.global.exception.CommonForbiddenException;
import online.bottler.global.exception.DomainException;
import online.bottler.mapletter.adaptor.out.persistence.entity.MapLetterEntity;
import online.bottler.mapletter.adaptor.out.persistence.entity.ReplyMapLetterEntity;
import online.bottler.mapletter.adaptor.out.persistence.repository.MapLetterJpaRepository;
import online.bottler.mapletter.adaptor.out.persistence.repository.ReplyMapLetterJpaRepository;
import online.bottler.mapletter.application.response.FindAllReceivedLetterResponse;
import online.bottler.mapletter.application.response.FindAllSentMapLetterResponse;
import online.bottler.mapletter.application.response.FindMapLetterResponse;
import online.bottler.mapletter.application.response.FindNearbyLettersResponse;
import online.bottler.mapletter.application.response.FindReceivedMapLetterResponse;
import online.bottler.mapletter.application.response.OneLetterResponse;
import online.bottler.mapletter.domain.MapLetterType;
import online.bottler.user.adapter.out.persistence.entity.UserEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@Import(RedisTestContainersConfig.class)
@Transactional
class MapLetterFacadeTest extends MapLetterApplicationTestHelper {

    @Autowired
    private MapLetterFacade mapLetterFacade;

    @Autowired
    private MapLetterJpaRepository mapLetterJpaRepository;

    @Autowired
    private ReplyMapLetterJpaRepository replyMapLetterJpaRepository;

    @AfterEach
    void tearDown() {
        mapLetterJpaRepository.deleteAllInBatch();
        replyMapLetterJpaRepository.deleteAllInBatch();
    }

    @DisplayName("거리와 상관 없이 보관된 편지 조회에 성공한다.")
    @Test
    void findArchiveLetterTest() {
        //given
        UserEntity savedUser = saveUser();

        Long userId = savedUser.getUserId();
        MapLetterEntity savedMapLetter = mapLetterJpaRepository.save(createMapLetterEntity(savedUser.getUserId()));

        //when
        OneLetterResponse archiveOneLetter
                = mapLetterFacade.findArchiveOneLetter(savedMapLetter.getMapLetterId(), userId);

        //then
        assertThat(archiveOneLetter.title()).isEqualTo(savedMapLetter.getTitle());
        assertThat(archiveOneLetter.profileImg()).isEqualTo(savedUser.getImageUrl());
    }

    @DisplayName("보관된 편지 조회 시 요청자와 편지 작성자가 동일하면 isOwner가 true를 반환한다.")
    @Test
    void returnsIsOwnerTrueWhenRequesterIsAuthorTest() {
        //given
        UserEntity savedUser = saveUser();

        Long userId = savedUser.getUserId();
        MapLetterEntity savedMapLetter = mapLetterJpaRepository.save(createMapLetterEntity(savedUser.getUserId()));

        //when
        OneLetterResponse archiveOneLetter
                = mapLetterFacade.findArchiveOneLetter(savedMapLetter.getMapLetterId(), userId);

        //then
        assertThat(archiveOneLetter.title()).isEqualTo(savedMapLetter.getTitle());
        assertThat(archiveOneLetter.profileImg()).isEqualTo(savedUser.getImageUrl());
        assertThat(archiveOneLetter.isOwner()).isTrue();
    }

    @DisplayName("보관된 편지 조회 시 요청자와 편지 작성자가 동일하지 않으면 isOwner가 false를 반환한다.")
    @Test
    void returnsIsOwnerFalseWhenRequesterIsNotAuthorTest() {
        //given
        UserEntity savedUser = saveUser();

        Long createUserId = savedUser.getUserId();
        Long requestUserId = 2L;
        MapLetterEntity savedMapLetter = mapLetterJpaRepository.save(createMapLetterEntity(createUserId));

        //when
        OneLetterResponse archiveOneLetter
                = mapLetterFacade.findArchiveOneLetter(savedMapLetter.getMapLetterId(), requestUserId);

        //then
        assertThat(archiveOneLetter.title()).isEqualTo(savedMapLetter.getTitle());
        assertThat(archiveOneLetter.profileImg()).isEqualTo(savedUser.getImageUrl());
        assertThat(archiveOneLetter.isOwner()).isFalse();
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
        List<FindNearbyLettersResponse> findNearbyLettersResponses
                = mapLetterFacade.guestFindNearByMapLetters(latitude, longitude);

        //then
        assertThat(findNearbyLettersResponses).hasSize(2);
        assertThat(findNearbyLettersResponses.get(0).createUserNickname()).isEqualTo(savedUser.getNickname());
    }

    @DisplayName("로그인 하지 않은 유저 편지 상세 조회시 반경 15M안에 있는 PUBLIC 편지인 경우 조회에 성공한다.")
    @Test
    void guestCanViewPublicLetterWithin15MTest() {
        //given
        UserEntity savedUser = saveUser();

        BigDecimal latitude = new BigDecimal("37.5665");
        BigDecimal longitude = new BigDecimal("126.9780");

        MapLetterEntity savedMapLetter = mapLetterJpaRepository.save(createMapLetterEntity(
                savedUser.getUserId(), new BigDecimal("37.5666"), new BigDecimal("126.9781"),
                MapLetterType.PUBLIC)); //14.8m

        //when
        OneLetterResponse oneLetterResponse
                = mapLetterFacade.guestFindOneMapLetter(savedMapLetter.getMapLetterId(), latitude, longitude);

        //then
        assertThat(oneLetterResponse.profileImg()).isEqualTo(savedUser.getImageUrl());
        assertThat(oneLetterResponse.title()).isEqualTo(savedMapLetter.getTitle());
    }

    @DisplayName("로그인 하지 않은 유저 편지 상세 조회시 반경 15M 밖에 있는 편지는 조회에 실패한다.")
    @Test
    void guestCannotViewLetterBeyond15MTest() {
        //given
        UserEntity savedUser = saveUser();

        BigDecimal latitude = new BigDecimal("37.5665");
        BigDecimal longitude = new BigDecimal("126.9780");

        MapLetterEntity savedMapLetter = mapLetterJpaRepository.save(createMapLetterEntity(
                savedUser.getUserId(), new BigDecimal("37.569679"), new BigDecimal("126.982011"),
                MapLetterType.PUBLIC)); //499.99m

        //when, then
        assertThatThrownBy(() ->
                mapLetterFacade.guestFindOneMapLetter(savedMapLetter.getMapLetterId(), latitude, longitude))
                .isInstanceOf(DomainException.class)
                .hasMessage("편지와의 거리가 멀어서 조회가 불가능합니다.");
    }

    @DisplayName("로그인 하지 않은 유저 편지 상세 조회시 PRIVATE 편지는 조회에 실패한다.")
    @Test
    void guestCannotViewPrivateLetterTest() {
        //given
        UserEntity savedUser = saveUser();

        BigDecimal latitude = new BigDecimal("37.5665");
        BigDecimal longitude = new BigDecimal("126.9780");

        MapLetterEntity savedMapLetter = mapLetterJpaRepository.save(createMapLetterEntity(
                savedUser.getUserId(), new BigDecimal("37.5666"), new BigDecimal("126.9781"),
                MapLetterType.PRIVATE)); //14.8m

        //when, then
        assertThatThrownBy(() ->
                mapLetterFacade.guestFindOneMapLetter(savedMapLetter.getMapLetterId(), latitude, longitude))
                .isInstanceOf(CommonForbiddenException.class)
                .hasMessage("해당 편지에 접근할 수 없습니다.");
    }

    @DisplayName("로그인 한 유저는 반경 15M 안에 있는 편지만 상세 조회가 가능하다.")
    @Test
    void userCanViewLetterWithin15mTest() {
        //given
        UserEntity savedUser = saveUser();

        BigDecimal latitude = new BigDecimal("37.5665");
        BigDecimal longitude = new BigDecimal("126.9780");

        MapLetterEntity savedMapLetter = mapLetterJpaRepository.save(createMapLetterEntity(
                savedUser.getUserId(), new BigDecimal("37.5666"), new BigDecimal("126.9781"),
                MapLetterType.PUBLIC)); //14.8m

        //when
        OneLetterResponse response = mapLetterFacade.findOneMapLetter(savedMapLetter.getMapLetterId(),
                savedUser.getUserId(), latitude, longitude);

        //then
        assertThat(response.profileImg()).isEqualTo(savedUser.getImageUrl());
        assertThat(response.title()).isEqualTo(savedMapLetter.getTitle());
    }

    @DisplayName("로그인 한 유저는 반경 15M 밖에 있는 편지는 상세 조회가 불가능하다.")
    @Test
    void userCannotViewLetterBeyond15mTest() {
        //given
        UserEntity savedUser = saveUser();

        BigDecimal latitude = new BigDecimal("37.5665");
        BigDecimal longitude = new BigDecimal("126.9780");

        MapLetterEntity savedMapLetter = mapLetterJpaRepository.save(createMapLetterEntity(
                savedUser.getUserId(), new BigDecimal("37.569679"), new BigDecimal("126.982011"),
                MapLetterType.PUBLIC)); //499.99m

        //when, then
        assertThatThrownBy(() -> mapLetterFacade.findOneMapLetter(savedMapLetter.getMapLetterId(),
                savedUser.getUserId(), latitude, longitude))
                .isInstanceOf(DomainException.class)
                .hasMessage("편지와의 거리가 멀어서 조회가 불가능합니다.");
    }

    @DisplayName("PRIVATE 편지의 경우, 타겟 유저가 조회를 요청한 유저인 경우 편지 상세 조회가 가능하다.")
    @Test
    void succeedsWhenTargetUserViewsPrivateLetterTest() {
        //given
        UserEntity targetUser = saveUser();
        UserEntity createUser = saveUser();

        BigDecimal latitude = new BigDecimal("37.5665");
        BigDecimal longitude = new BigDecimal("126.9780");

        MapLetterEntity savedMapLetter = mapLetterJpaRepository.save(createMapLetterEntity(
                createUser.getUserId(), new BigDecimal("37.5666"), new BigDecimal("126.9781"),
                MapLetterType.PRIVATE, targetUser.getUserId())); //14.8m

        //when
        OneLetterResponse response = mapLetterFacade.findOneMapLetter(savedMapLetter.getMapLetterId(),
                targetUser.getUserId(), latitude, longitude);

        //then
        assertThat(response.profileImg()).isEqualTo(createUser.getImageUrl());
        assertThat(response.title()).isEqualTo(savedMapLetter.getTitle());
    }

    @DisplayName("PRIVATE 편지의 경우, 편지를 작성한 유저가 조회를 요청한 유저인 경우 조회가 가능하다.")
    @Test
    void succeedsWhenAuthorViewsOwnPrivateLetterTest() {
        //given
        UserEntity targetUser = saveUser();
        UserEntity createUser = saveUser();

        BigDecimal latitude = new BigDecimal("37.5665");
        BigDecimal longitude = new BigDecimal("126.9780");

        MapLetterEntity savedMapLetter = mapLetterJpaRepository.save(createMapLetterEntity(
                createUser.getUserId(), new BigDecimal("37.5666"), new BigDecimal("126.9781"),
                MapLetterType.PRIVATE, targetUser.getUserId())); //14.8m

        //when
        OneLetterResponse response = mapLetterFacade.findOneMapLetter(savedMapLetter.getMapLetterId(),
                createUser.getUserId(), latitude, longitude);

        //then
        assertThat(response.profileImg()).isEqualTo(createUser.getImageUrl());
        assertThat(response.title()).isEqualTo(savedMapLetter.getTitle());
    }

    @DisplayName("PRIVATE 편지의 경우, 타겟 유저 혹은 편지를 작성한 유저가 조회를 요청한 유저가 아닌 경우 조회가 불가능하다.")
    @Test
    void cannotViewPrivateLetterIfNotAuthorOrTargetUserTest() {
        //given
        UserEntity targetUser = saveUser();
        UserEntity createUser = saveUser();
        UserEntity user = saveUser();

        BigDecimal latitude = new BigDecimal("37.5665");
        BigDecimal longitude = new BigDecimal("126.9780");

        MapLetterEntity savedMapLetter = mapLetterJpaRepository.save(createMapLetterEntity(
                createUser.getUserId(), new BigDecimal("37.5666"), new BigDecimal("126.9781"),
                MapLetterType.PRIVATE, targetUser.getUserId())); //14.8m

        //when
        assertThatThrownBy(() -> mapLetterFacade.findOneMapLetter(savedMapLetter.getMapLetterId(),
                user.getUserId(), latitude, longitude))
                .isInstanceOf(CommonForbiddenException.class)
                .hasMessage("편지를 볼 수 있는 권한이 없습니다.");
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
        List<FindNearbyLettersResponse> responses
                = mapLetterFacade.findNearByMapLetters(latitude, longitude, targetUser.getUserId());

        //then
        assertThat(responses).hasSize(3);
    }

    @DisplayName("블락, 삭제되지 않은 보낸 편지들(지도 편지, 답장 편지)을 내림차순으로 조회한다.")
    @Test
    void findSentLettersTest() {
        //given
        UserEntity createUser = saveUser();
        UserEntity sourceLetterCreateUser = saveUser();

        mapLetterJpaRepository.save(createMapLetterEntity(createUser.getUserId()));
        mapLetterJpaRepository.save(createMapLetterEntity(createUser.getUserId()));
        MapLetterEntity sourceLetter = mapLetterJpaRepository.save(
                createMapLetterEntity(sourceLetterCreateUser.getUserId()));
        replyMapLetterJpaRepository.save(
                createReplyMapLetterEntity(createUser.getUserId(), sourceLetter.getMapLetterId()));

        mapLetterJpaRepository.save(createMapLetterEntity(createUser.getUserId(), true, false));
        mapLetterJpaRepository.save(createMapLetterEntity(createUser.getUserId(), false, true));

        //when
        Page<FindMapLetterResponse> responses = mapLetterFacade.findSentMapLetters(1, 9, createUser.getUserId());

        //then
        assertThat(responses.getContent()).hasSize(3);
        assertThat(responses.getContent().get(0).title()).isEqualTo("Re: " + sourceLetter.getTitle());
    }

    @DisplayName("블락, 삭제되지 않은 받은 편지들(타겟 편지, 답장 편지)을 내림차순으로 조회한다.")
    @Test
    void findReceivedMapLettersTest() {
        //given
        UserEntity targetUser = saveUser();
        UserEntity createUser = saveUser();

        mapLetterJpaRepository.save(createMapLetterEntity(createUser.getUserId(), targetUser.getUserId()));
        MapLetterEntity sourceLetter = mapLetterJpaRepository.save(createMapLetterEntity(targetUser.getUserId()));
        replyMapLetterJpaRepository.save(
                createReplyMapLetterEntity(createUser.getUserId(), sourceLetter.getMapLetterId()));

        mapLetterJpaRepository.save(createMapLetterEntity(targetUser.getUserId(), createUser.getUserId()));
        mapLetterJpaRepository.save(createMapLetterEntity(createUser.getUserId(), targetUser.getUserId(), true, false));
        mapLetterJpaRepository.save(createMapLetterEntity(createUser.getUserId(), targetUser.getUserId(), false, true));

        //when
        Page<FindReceivedMapLetterResponse> responses
                = mapLetterFacade.findReceivedMapLetters(1, 9, targetUser.getUserId());

        //then
        assertThat(responses.getContent()).hasSize(2);
    }

    @DisplayName("블락, 삭제되지 않은 보낸 편지들(답장 제외)을 조회한다.")
    @Test
    void findSentMapLettersExcludingBlockedOrDeleted() {
        //given
        UserEntity createUser = saveUser();
        UserEntity sourceLetterCreateUser = saveUser();

        mapLetterJpaRepository.save(createMapLetterEntity(createUser.getUserId()));
        mapLetterJpaRepository.save(createMapLetterEntity(createUser.getUserId()));

        MapLetterEntity sourceLetter = mapLetterJpaRepository.save(
                createMapLetterEntity(sourceLetterCreateUser.getUserId()));
        replyMapLetterJpaRepository.save(
                createReplyMapLetterEntity(createUser.getUserId(), sourceLetter.getMapLetterId()));
        mapLetterJpaRepository.save(createMapLetterEntity(createUser.getUserId(), true, false));
        mapLetterJpaRepository.save(createMapLetterEntity(createUser.getUserId(), false, true));

        //when
        Page<FindAllSentMapLetterResponse> responses = mapLetterFacade.findAllSentMapLetters(1, 9,
                createUser.getUserId());

        //then
        assertThat(responses.getContent()).hasSize(2);
    }

    @DisplayName("블락, 삭제되지 않은 받은 타겟 편지들을 조회한다.")
    @Test
    void findReceivedLettersExcludingBlockedAndDeleted() {
        //given
        UserEntity targetUser = saveUser();
        UserEntity createUser = saveUser();

        mapLetterJpaRepository.save(createMapLetterEntity(createUser.getUserId(), targetUser.getUserId()));

        mapLetterJpaRepository.save(createMapLetterEntity(targetUser.getUserId(), createUser.getUserId()));
        mapLetterJpaRepository.save(createMapLetterEntity(createUser.getUserId(), targetUser.getUserId(), true, false));
        mapLetterJpaRepository.save(createMapLetterEntity(createUser.getUserId(), targetUser.getUserId(), false, true));

        //when
        Page<FindAllReceivedLetterResponse> responses = mapLetterFacade.findAllReceivedLetters(1, 9,
                targetUser.getUserId());

        //then
        assertThat(responses.getContent()).hasSize(1);
    }

    private ReplyMapLetterEntity createReplyMapLetterEntity(Long createUserId, Long sourceLetterId) {
        return ReplyMapLetterEntity.builder()
                .sourceLetterId(sourceLetterId)
                .font("font")
                .paper("paper")
                .label("label")
                .isBlocked(false)
                .isDeleted(false)
                .createdAt(LocalDateTime.of(2025, 7, 11, 17, 21))
                .updatedAt(LocalDateTime.of(2025, 7, 11, 17, 21))
                .createUserId(createUserId)
                .content("content")
                .isRecipientDeleted(false)
                .build();
    }
}
