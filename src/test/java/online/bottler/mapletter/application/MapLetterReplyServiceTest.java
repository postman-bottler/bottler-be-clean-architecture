package online.bottler.mapletter.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;

import java.time.LocalDateTime;
import java.util.List;
import online.bottler.RedisTestContainersConfig;
import online.bottler.global.exception.AdaptorException;
import online.bottler.global.exception.ApplicationException;
import online.bottler.global.exception.CommonForbiddenException;
import online.bottler.global.exception.DomainException;
import online.bottler.mapletter.adaptor.out.persistence.entity.MapLetterEntity;
import online.bottler.mapletter.adaptor.out.persistence.entity.ReplyMapLetterEntity;
import online.bottler.mapletter.adaptor.out.persistence.repository.MapLetterJpaRepository;
import online.bottler.mapletter.adaptor.out.persistence.repository.ReplyMapLetterJpaRepository;
import online.bottler.mapletter.application.command.CreateReplyMapLetterCommand;
import online.bottler.mapletter.application.command.DeleteReplyMapLettersCommand;
import online.bottler.mapletter.application.response.CheckReplyMapLetterResponse;
import online.bottler.mapletter.application.response.FindAllReceivedReplyLetterResponse;
import online.bottler.mapletter.application.response.FindAllReplyMapLettersResponse;
import online.bottler.mapletter.application.response.FindAllSentReplyMapLetterResponse;
import online.bottler.mapletter.application.response.OneReplyLetterResponse;
import online.bottler.mapletter.domain.MapLetter;
import online.bottler.mapletter.domain.ReplyMapLetter;
import online.bottler.user.adapter.out.persistence.entity.UserEntity;
import org.assertj.core.groups.Tuple;
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
class MapLetterReplyServiceTest extends MapLetterApplicationTestHelper {

    @Autowired
    private MapLetterReplyService mapLetterReplyService;

    @Autowired
    private MapLetterJpaRepository mapLetterJpaRepository;

    @Autowired
    private ReplyMapLetterJpaRepository replyMapLetterJpaRepository;

    @AfterEach
    void tearDown() {
        mapLetterJpaRepository.deleteAllInBatch();
        replyMapLetterJpaRepository.deleteAllInBatch();
    }

    @DisplayName("편지에 답장을 처음 보내는 경우 답장에 성공한다.")
    @Test
    void createReplyMapLetterTest() {
        //given
        MapLetterEntity mapLetter = mapLetterJpaRepository.save(createMapLetterEntity());
        CreateReplyMapLetterCommand replyMapLetterCommand = createReplyMapLetterCommand(mapLetter);
        UserEntity user = saveUser();

        //when
        ReplyMapLetter replyMapLetter = mapLetterReplyService
                .createReplyMapLetter(replyMapLetterCommand, user.getUserId(), MapLetterEntity.toDomain(mapLetter));

        //then
        assertThat(replyMapLetter.getSourceLetterId()).isEqualTo(mapLetter.getMapLetterId());
        assertThat(replyMapLetter.getCreateUserId()).isEqualTo(user.getUserId());
    }

    @DisplayName("편지에 답장을 이미 보낸 경우 예외가 발생한다.")
    @Test
    void alreadyRepliedToMapLetterTest() {
        //given
        MapLetterEntity mapLetter = mapLetterJpaRepository.save(createMapLetterEntity());
        CreateReplyMapLetterCommand replyMapLetterCommand = createReplyMapLetterCommand(mapLetter);
        UserEntity user = saveUser();

        mapLetterReplyService.createReplyMapLetter(replyMapLetterCommand, user.getUserId(),
                MapLetterEntity.toDomain(mapLetter));

        //when, then
        assertThatThrownBy(() -> mapLetterReplyService
                .createReplyMapLetter(replyMapLetterCommand, user.getUserId(), MapLetterEntity.toDomain(mapLetter)))
                .isInstanceOf(ApplicationException.class)
                .hasMessage("해당 지도 편지에 이미 답장을 했습니다.");
    }

    @DisplayName("원본 편지 조회에 성공한다.")
    @Test
    void findSourceMapLetterTest() {
        //given
        MapLetterEntity mapLetter = mapLetterJpaRepository.save(createMapLetterEntity());

        //when
        MapLetter sourceMapLetter = mapLetterReplyService.findSourceMapLetter(mapLetter.getMapLetterId());

        //then
        assertThat(sourceMapLetter.getId()).isEqualTo(mapLetter.getMapLetterId());
    }

    @DisplayName("원본 편지가 없을 경우 예외가 발생한다.")
    @Test
    void sourceMapLetterNotFoundTest() {
        //when, then
        assertThatThrownBy(() -> mapLetterReplyService.findSourceMapLetter(11L))
                .isInstanceOf(AdaptorException.class)
                .hasMessage("원본 편지를 찾을 수 없습니다. 편지가 존재하지 않거나 삭제되었습니다.");
    }

    @DisplayName("원본 편지가 삭제되었을 경우 예외가 발생한다.")
    @Test
    void sourceMapLetterDeletedTest() {
        //given
        MapLetterEntity mapLetter = mapLetterJpaRepository.save(createMapLetterEntity(1L, true, false));

        //when, then
        assertThatThrownBy(() -> mapLetterReplyService.findSourceMapLetter(mapLetter.getMapLetterId()))
                .isInstanceOf(AdaptorException.class)
                .hasMessage("원본 편지를 찾을 수 없습니다. 편지가 존재하지 않거나 삭제되었습니다.");
    }

    @DisplayName("원본 편지가 블락되었을 경우 예외가 발생한다.")
    @Test
    void sourceMapLetterBlockedTest() {
        //given
        MapLetterEntity mapLetter = mapLetterJpaRepository.save(createMapLetterEntity(1L, false, true));

        //when, then
        assertThatThrownBy(() -> mapLetterReplyService.findSourceMapLetter(mapLetter.getMapLetterId()))
                .isInstanceOf(AdaptorException.class)
                .hasMessage("원본 편지를 찾을 수 없습니다. 편지가 존재하지 않거나 삭제되었습니다.");
    }

    @DisplayName("편지의 전체 답장 편지들을 조회한다.")
    @Test
    void findAllReplyMapLettersTest() {
        //given
        UserEntity sourceLetterCreateUser = saveUser();
        UserEntity user1 = saveUser();
        UserEntity user2 = saveUser();
        UserEntity user3 = saveUser();
        MapLetterEntity mapLetter = mapLetterJpaRepository.save(
                createMapLetterEntity(sourceLetterCreateUser.getUserId()));

        replyMapLetterJpaRepository.save(createReplyMapLetter(mapLetter.getMapLetterId(), user1.getUserId()));
        replyMapLetterJpaRepository.save(createReplyMapLetter(mapLetter.getMapLetterId(), user2.getUserId()));
        replyMapLetterJpaRepository.save(createReplyMapLetter(mapLetter.getMapLetterId(), user3.getUserId()));

        //when
        Page<FindAllReplyMapLettersResponse> replyMapLetters = mapLetterReplyService.findAllReplyMapLetters
                (1, 9, mapLetter.getMapLetterId(), sourceLetterCreateUser.getUserId());

        //then
        assertThat(replyMapLetters.getContent()).hasSize(3)
                .extracting("label")
                .containsExactlyInAnyOrder(
                        mapLetter.getLabel(),
                        mapLetter.getLabel(),
                        mapLetter.getLabel()
                );
    }

    @DisplayName("편지 답장 조회시 원본 편지가 없으면 예외가 발생한다.")
    @Test
    void sourceLetterNotFoundTest() {
        //given
        UserEntity user = saveUser();

        //when, then
        assertThatThrownBy(() -> mapLetterReplyService.findAllReplyMapLetters(1, 9, 1000L, user.getUserId()))
                .isInstanceOf(AdaptorException.class)
                .hasMessage("해당 편지를 찾을 수 없습니다.");
    }

    @DisplayName("원본 편지를 작성한 유저가 아닌 사람이 편지 답장을 조회하면 예외가 발생한다.")
    @Test
    void nonOwnerTriesToGetReplyLetterTest() {
        //given
        UserEntity user = saveUser();
        UserEntity createUser = saveUser();

        MapLetterEntity mapLetter = mapLetterJpaRepository.save(createMapLetterEntity(createUser.getUserId()));

        replyMapLetterJpaRepository.save(createReplyMapLetter(mapLetter.getMapLetterId()));

        //when, then
        assertThatThrownBy(
                () -> mapLetterReplyService.findAllReplyMapLetters(1, 9, mapLetter.getMapLetterId(), user.getUserId()))
                .isInstanceOf(CommonForbiddenException.class)
                .hasMessage("편지를 볼 수 있는 권한이 없습니다.");
    }

    @DisplayName("답장을 작성한 유저가 답장 편지 상세 조회시 조회에 성공한다.")
    @Test
    void findReplyMapLetterByReplyLetterAuthorTest() {
        //given
        UserEntity sourceLetterCreateUser = saveUser();
        UserEntity replyLetterCreateUser = saveUser();

        MapLetterEntity mapLetter =
                mapLetterJpaRepository.save(createMapLetterEntity(sourceLetterCreateUser.getUserId()));
        ReplyMapLetterEntity replyMapLetter =
                replyMapLetterJpaRepository.save(
                        createReplyMapLetter(mapLetter.getMapLetterId(), replyLetterCreateUser.getUserId()));

        //when
        OneReplyLetterResponse response = mapLetterReplyService.findReplyMapLetter(replyMapLetter.getReplyLetterId(),
                replyLetterCreateUser.getUserId());

        //then
        assertThat(response.sourceLetterId()).isEqualTo(mapLetter.getMapLetterId());
        assertThat(response.isOwner()).isTrue();
    }

    @DisplayName("원본 편지를 작성한 유저가 답장 편지 상세 조회시 조회에 성공한다.")
    @Test
    void findReplyMapLetterBySourceLetterAuthorTest() {
        //given
        UserEntity sourceLetterCreateUser = saveUser();
        UserEntity replyLetterCreateUser = saveUser();

        MapLetterEntity mapLetter =
                mapLetterJpaRepository.save(createMapLetterEntity(sourceLetterCreateUser.getUserId()));
        ReplyMapLetterEntity replyMapLetter =
                replyMapLetterJpaRepository.save(
                        createReplyMapLetter(mapLetter.getMapLetterId(), replyLetterCreateUser.getUserId()));

        //when
        OneReplyLetterResponse response = mapLetterReplyService.findReplyMapLetter(replyMapLetter.getReplyLetterId(),
                sourceLetterCreateUser.getUserId());

        //then
        assertThat(response.sourceLetterId()).isEqualTo(mapLetter.getMapLetterId());
        assertThat(response.isOwner()).isFalse();
    }

    @DisplayName("답장 상세 조회시 답장 편지가 없으면 예외가 발생한다.")
    @Test
    void replyLetterNotFoundTest() {
        //when, then
        assertThatThrownBy(() -> mapLetterReplyService.findReplyMapLetter(1000L, 12L))
                .isInstanceOf(AdaptorException.class)
                .hasMessage("해당 편지를 찾을 수 없습니다.");
    }

    @DisplayName("원본 편지를 작성한 유저 혹은 답장 편지를 작성한 유저가 아니면 예외가 발생한다.")
    @Test
    void notAuthorOfSourceOrReplyLetter() {
        //given
        UserEntity sourceLetterCreateUser = saveUser();
        UserEntity replyLetterCreateUser = saveUser();
        UserEntity user = saveUser();

        MapLetterEntity mapLetter =
                mapLetterJpaRepository.save(createMapLetterEntity(sourceLetterCreateUser.getUserId()));
        ReplyMapLetterEntity replyMapLetter =
                replyMapLetterJpaRepository.save(
                        createReplyMapLetter(mapLetter.getMapLetterId(), replyLetterCreateUser.getUserId()));

        //when, then
        assertThatThrownBy(
                () -> mapLetterReplyService.findReplyMapLetter(replyMapLetter.getReplyLetterId(), user.getUserId()))
                .isInstanceOf(CommonForbiddenException.class)
                .hasMessage("편지를 볼 수 있는 권한이 없습니다.");
    }

    @DisplayName("답장 편지가 삭제되었으면 예외가 발생한다.")
    @Test
    void replyLetterDeletedTest() {
        //given
        UserEntity sourceLetterCreateUser = saveUser();
        UserEntity replyLetterCreateUser = saveUser();

        MapLetterEntity mapLetter =
                mapLetterJpaRepository.save(createMapLetterEntity(sourceLetterCreateUser.getUserId()));
        ReplyMapLetterEntity replyMapLetter =
                replyMapLetterJpaRepository.save(createReplyMapLetter(mapLetter.getMapLetterId(),
                        replyLetterCreateUser.getUserId(), true, false));

        //when, then
        assertThatThrownBy(() -> mapLetterReplyService.findReplyMapLetter(replyMapLetter.getReplyLetterId(),
                replyLetterCreateUser.getUserId()))
                .isInstanceOf(DomainException.class)
                .hasMessage("해당 편지는 삭제되었습니다.");
    }

    @DisplayName("답장 편지가 블락되었으면 예외가 발생한다.")
    @Test
    void replyLetterBlockedTest() {
        //given
        UserEntity sourceLetterCreateUser = saveUser();
        UserEntity replyLetterCreateUser = saveUser();

        MapLetterEntity mapLetter =
                mapLetterJpaRepository.save(createMapLetterEntity(sourceLetterCreateUser.getUserId()));
        ReplyMapLetterEntity replyMapLetter =
                replyMapLetterJpaRepository.save(createReplyMapLetter(mapLetter.getMapLetterId(),
                        replyLetterCreateUser.getUserId(), false, true));

        //when, then
        assertThatThrownBy(() -> mapLetterReplyService.findReplyMapLetter(replyMapLetter.getReplyLetterId(),
                replyLetterCreateUser.getUserId()))
                .isInstanceOf(DomainException.class)
                .hasMessage("해당 편지는 신고당한 편지입니다.");
    }

    @DisplayName("원본 편지가 삭제되었으면 예외가 발생한다.")
    @Test
    void sourceLetterDeletedTest() {
        //given
        UserEntity sourceLetterCreateUser = saveUser();
        UserEntity replyLetterCreateUser = saveUser();

        MapLetterEntity mapLetter =
                mapLetterJpaRepository.save(createMapLetterEntity(sourceLetterCreateUser.getUserId(), true, false));
        ReplyMapLetterEntity replyMapLetter =
                replyMapLetterJpaRepository.save(createReplyMapLetter(mapLetter.getMapLetterId(),
                        replyLetterCreateUser.getUserId(), true, false));

        //when, then
        assertThatThrownBy(() -> mapLetterReplyService.findReplyMapLetter(replyMapLetter.getReplyLetterId(),
                replyLetterCreateUser.getUserId()))
                .isInstanceOf(DomainException.class)
                .hasMessage("해당 편지는 삭제되었습니다.");
    }

    @DisplayName("원본 편지가 블락되었으면 예외가 발생한다.")
    @Test
    void sourceLetterBlockedTest() {
        //given
        UserEntity sourceLetterCreateUser = saveUser();
        UserEntity replyLetterCreateUser = saveUser();

        MapLetterEntity mapLetter =
                mapLetterJpaRepository.save(createMapLetterEntity(sourceLetterCreateUser.getUserId(), true, false));
        ReplyMapLetterEntity replyMapLetter =
                replyMapLetterJpaRepository.save(createReplyMapLetter(mapLetter.getMapLetterId(),
                        replyLetterCreateUser.getUserId(), false, true));

        //when, then
        assertThatThrownBy(() -> mapLetterReplyService.findReplyMapLetter(replyMapLetter.getReplyLetterId(),
                replyLetterCreateUser.getUserId()))
                .isInstanceOf(DomainException.class)
                .hasMessage("해당 편지는 신고당한 편지입니다.");
    }

    @DisplayName("답장을 작성한 유저가 삭제를 요청하면 답장 삭제에 성공한다.")
    @Test
    void deleteReplyMapLetterTest() {
        //given
        UserEntity sourceLetterCreateUser = saveUser();
        UserEntity replyLetterCreateUser = saveUser();

        MapLetterEntity mapLetter1 = mapLetterJpaRepository.save(
                createMapLetterEntity(sourceLetterCreateUser.getUserId()));
        MapLetterEntity mapLetter2 = mapLetterJpaRepository.save(
                createMapLetterEntity(sourceLetterCreateUser.getUserId()));
        MapLetterEntity mapLetter3 = mapLetterJpaRepository.save(
                createMapLetterEntity(sourceLetterCreateUser.getUserId()));

        ReplyMapLetterEntity replyMapLetter1 =
                replyMapLetterJpaRepository.save(
                        createReplyMapLetter(mapLetter1.getMapLetterId(), replyLetterCreateUser.getUserId()));
        ReplyMapLetterEntity replyMapLetter2 =
                replyMapLetterJpaRepository.save(
                        createReplyMapLetter(mapLetter2.getMapLetterId(), replyLetterCreateUser.getUserId()));
        ReplyMapLetterEntity replyMapLetter3 =
                replyMapLetterJpaRepository.save(
                        createReplyMapLetter(mapLetter3.getMapLetterId(), replyLetterCreateUser.getUserId()));

        Page<FindAllSentReplyMapLetterResponse> beforeDelete = mapLetterReplyService.findAllSentReplyMapLetters(1, 9,
                replyLetterCreateUser.getUserId());

        assertThat(beforeDelete.getContent()).hasSize(3);

        DeleteReplyMapLettersCommand command = new DeleteReplyMapLettersCommand(
                List.of(replyMapLetter1.getReplyLetterId(), replyMapLetter2.getReplyLetterId()));

        //when
        mapLetterReplyService.deleteReplyMapLetter(command, replyLetterCreateUser.getUserId());

        //then
        Page<FindAllSentReplyMapLetterResponse> afterDelete = mapLetterReplyService.findAllSentReplyMapLetters(1, 9,
                replyLetterCreateUser.getUserId());
        assertThat(afterDelete.getContent()).hasSize(1);
    }

    @DisplayName("답장을 작성한 유저아닌 사람이 답장 삭제를 요청하면 예외가 발생한다.")
    @Test
    void nonAuthorTriesToDeleteReplyLetterTest() {
        //given
        UserEntity sourceLetterCreateUser = saveUser();
        UserEntity replyLetterCreateUser = saveUser();

        MapLetterEntity mapLetter = mapLetterJpaRepository.save(
                createMapLetterEntity(sourceLetterCreateUser.getUserId()));

        ReplyMapLetterEntity replyMapLetter = replyMapLetterJpaRepository.save(
                createReplyMapLetter(mapLetter.getMapLetterId(), replyLetterCreateUser.getUserId()));

        DeleteReplyMapLettersCommand command = new DeleteReplyMapLettersCommand(
                List.of(replyMapLetter.getReplyLetterId()));

        //when, then
        assertThatThrownBy(
                () -> mapLetterReplyService.deleteReplyMapLetter(command, sourceLetterCreateUser.getUserId()))
                .isInstanceOf(CommonForbiddenException.class)
                .hasMessage("편지를 삭제 할 권한이 없습니다. 편지 삭제에 실패하였습니다.");
    }

    @DisplayName("답장을 작성한 유저아닌 사람이 답장 삭제를 요청하면 예외가 발생한다.")
    @Test
    void nonAuthorTriesToDeleteReplyLetterTest2() {
        //given
        UserEntity sourceLetterCreateUser = saveUser();
        UserEntity replyLetterCreateUser = saveUser();
        UserEntity user = saveUser();

        MapLetterEntity mapLetter = mapLetterJpaRepository.save(
                createMapLetterEntity(sourceLetterCreateUser.getUserId()));

        ReplyMapLetterEntity replyMapLetter = replyMapLetterJpaRepository.save(
                createReplyMapLetter(mapLetter.getMapLetterId(), replyLetterCreateUser.getUserId()));

        DeleteReplyMapLettersCommand command = new DeleteReplyMapLettersCommand(
                List.of(replyMapLetter.getReplyLetterId()));

        //when, then
        assertThatThrownBy(() -> mapLetterReplyService.deleteReplyMapLetter(command, user.getUserId()))
                .isInstanceOf(CommonForbiddenException.class)
                .hasMessage("편지를 삭제 할 권한이 없습니다. 편지 삭제에 실패하였습니다.");
    }

    @DisplayName("답장이 삭제되었으면 답장 삭제에 실패한다.")
    @Test
    void deletingAlreadyDeletedReplyLetterTest() {
        UserEntity sourceLetterCreateUser = saveUser();
        UserEntity replyLetterCreateUser = saveUser();

        MapLetterEntity mapLetter = mapLetterJpaRepository.save(
                createMapLetterEntity(sourceLetterCreateUser.getUserId()));

        ReplyMapLetterEntity replyMapLetter = replyMapLetterJpaRepository.save(
                createReplyMapLetter(mapLetter.getMapLetterId(), replyLetterCreateUser.getUserId(), true, false));

        DeleteReplyMapLettersCommand command = new DeleteReplyMapLettersCommand(
                List.of(replyMapLetter.getReplyLetterId()));

        //when, then
        assertThatThrownBy(() -> mapLetterReplyService.deleteReplyMapLetter(command, replyLetterCreateUser.getUserId()))
                .isInstanceOf(DomainException.class)
                .hasMessage("해당 편지는 삭제되었습니다.");
    }

    @DisplayName("답장이 블락되었으면 답장 삭제에 실패한다.")
    @Test
    void deletingAlreadyBlockedReplyLetterTest() {
        UserEntity sourceLetterCreateUser = saveUser();
        UserEntity replyLetterCreateUser = saveUser();

        MapLetterEntity mapLetter = mapLetterJpaRepository.save(
                createMapLetterEntity(sourceLetterCreateUser.getUserId()));

        ReplyMapLetterEntity replyMapLetter = replyMapLetterJpaRepository.save(
                createReplyMapLetter(mapLetter.getMapLetterId(), replyLetterCreateUser.getUserId(), false, true));

        DeleteReplyMapLettersCommand command = new DeleteReplyMapLettersCommand(
                List.of(replyMapLetter.getReplyLetterId()));

        //when, then
        assertThatThrownBy(() -> mapLetterReplyService.deleteReplyMapLetter(command, replyLetterCreateUser.getUserId()))
                .isInstanceOf(DomainException.class)
                .hasMessage("해당 편지는 신고당한 편지입니다.");
    }

    @DisplayName("보낸 답장 편지를 전체 조회한다.")
    @Test
    void findAllSentReplyMapLettersTest() {
        //given
        UserEntity sourceLetterCreateUser = saveUser();
        UserEntity replyLetterCreateUser = saveUser();

        MapLetterEntity mapLetter1 = mapLetterJpaRepository.save(
                createMapLetterEntity(sourceLetterCreateUser.getUserId()));
        MapLetterEntity mapLetter2 = mapLetterJpaRepository.save(
                createMapLetterEntity(sourceLetterCreateUser.getUserId()));

        replyMapLetterJpaRepository.save(
                createReplyMapLetter(mapLetter1.getMapLetterId(), replyLetterCreateUser.getUserId()));
        replyMapLetterJpaRepository.save(
                createReplyMapLetter(mapLetter2.getMapLetterId(), replyLetterCreateUser.getUserId()));

        //when
        Page<FindAllSentReplyMapLetterResponse> responses = mapLetterReplyService.findAllSentReplyMapLetters(
                1, 9, replyLetterCreateUser.getUserId());

        //then
        assertThat(responses.getContent()).hasSize(2)
                .extracting("title", "label")
                .containsExactlyInAnyOrder(
                        tuple("Re: " + mapLetter1.getTitle(), mapLetter1.getLabel()),
                        tuple("Re: " + mapLetter2.getTitle(), mapLetter2.getLabel())
                );
    }

    @DisplayName("받은 답장 편지들을 전체 조회한다.")
    @Test
    void findAllReceivedReplyMapLettersTest() {
        //given
        UserEntity sourceLetterCreateUser = saveUser();

        UserEntity replyLetterCreateUser1 = saveUser();
        UserEntity replyLetterCreateUser2 = saveUser();

        MapLetterEntity mapLetter = mapLetterJpaRepository.save(
                createMapLetterEntity(sourceLetterCreateUser.getUserId()));

        replyMapLetterJpaRepository.save(
                createReplyMapLetter(mapLetter.getMapLetterId(), replyLetterCreateUser1.getUserId()));
        replyMapLetterJpaRepository.save(
                createReplyMapLetter(mapLetter.getMapLetterId(), replyLetterCreateUser2.getUserId()));

        //when
        Page<FindAllReceivedReplyLetterResponse> responses = mapLetterReplyService.findAllReceivedReplyMapLetters(
                1, 9, sourceLetterCreateUser.getUserId());

        //then
        assertThat(responses.getContent()).hasSize(2)
                .extracting("title", "label")
                .containsExactlyInAnyOrder(
                        tuple("Re: " + mapLetter.getTitle(),mapLetter.getLabel()),
                        tuple("Re: " + mapLetter.getTitle(),mapLetter.getLabel())
                );
    }

    @DisplayName("해당 편지에 답장을 보냈으면 true를 반환한다.")
    @Test
    void checkReplyMapLetterTest1() {
        //given
        UserEntity sourceLetterCreateUser = saveUser();
        UserEntity replyLetterCreateUser = saveUser();

        MapLetterEntity mapLetter = mapLetterJpaRepository.save(
                createMapLetterEntity(sourceLetterCreateUser.getUserId()));
        ReplyMapLetterEntity replyMapLetter = replyMapLetterJpaRepository.save(
                createReplyMapLetter(mapLetter.getMapLetterId(), replyLetterCreateUser.getUserId()));

        //when
        CheckReplyMapLetterResponse response = mapLetterReplyService.checkReplyMapLetter(
                mapLetter.getMapLetterId(), replyLetterCreateUser.getUserId());

        //then
        assertThat(response.isReplied()).isTrue();
    }

    @DisplayName("해당 편지에 답장을 보내지 않았으면 false를 반환한다.")
    @Test
    void checkReplyMapLetterTest2() {
        //given
        UserEntity sourceLetterCreateUser = saveUser();
        UserEntity replyLetterCreateUser = saveUser();
        UserEntity user = saveUser();

        MapLetterEntity mapLetter = mapLetterJpaRepository.save(
                createMapLetterEntity(sourceLetterCreateUser.getUserId()));
        ReplyMapLetterEntity replyMapLetter = replyMapLetterJpaRepository.save(
                createReplyMapLetter(mapLetter.getMapLetterId(), replyLetterCreateUser.getUserId()));

        //when
        CheckReplyMapLetterResponse response = mapLetterReplyService.checkReplyMapLetter(
                replyMapLetter.getReplyLetterId(), user.getUserId());

        //then
        assertThat(response.isReplied()).isFalse();
    }

    private CreateReplyMapLetterCommand createReplyMapLetterCommand(MapLetterEntity mapLetter) {
        return new CreateReplyMapLetterCommand(mapLetter.getMapLetterId(),
                "content", "font", "paper", "label");
    }

    private ReplyMapLetterEntity createReplyMapLetter(Long mapLetterId, Long createUserId, boolean isDeleted,
                                                      boolean isBlocked) {
        return ReplyMapLetterEntity.builder()
                .sourceLetterId(mapLetterId)
                .font("font")
                .paper("paper")
                .label("label")
                .content("content")
                .isBlocked(isBlocked)
                .isDeleted(isDeleted)
                .createdAt(LocalDateTime.of(2025, 07, 15, 20, 40))
                .updatedAt(LocalDateTime.of(2025, 07, 15, 20, 40))
                .createUserId(createUserId)
                .isRecipientDeleted(false)
                .build();
    }

    private ReplyMapLetterEntity createReplyMapLetter(Long mapLetterId, Long createUserId) {
        return createReplyMapLetter(mapLetterId, createUserId, false, false);
    }

    private ReplyMapLetterEntity createReplyMapLetter(Long mapLetterId) {
        return createReplyMapLetter(mapLetterId, 1000L);
    }
}
