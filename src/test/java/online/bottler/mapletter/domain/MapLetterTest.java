package online.bottler.mapletter.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import online.bottler.global.exception.CommonForbiddenException;
import online.bottler.global.exception.DomainException;
import online.bottler.mapletter.domain.policy.MapLetterPolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.InstanceOfAssertFactories.map;

class MapLetterTest {

    @DisplayName("편지 삭제에 성공한다.")
    @Test
    void deleteTest() {
        //given
        MapLetter publicMapLetter = createMapLetter(CreateMapLetterType.PUBLIC);
        MapLetter privateMapLetter = createMapLetter(CreateMapLetterType.PRIVATE);

        //when
        publicMapLetter.updateDelete(true);
        privateMapLetter.updateDelete(true);

        //then
        assertThat(publicMapLetter.isDeleted()).isTrue();
        assertThat(privateMapLetter.isDeleted()).isTrue();
    }

    @DisplayName("받은 사람이 편지 삭제에 성공한다.")
    @Test
    void recipientDeleteTest() {
        //given
        MapLetter publicMapLetter = createMapLetter(CreateMapLetterType.PUBLIC);
        MapLetter privateMapLetter = createMapLetter(CreateMapLetterType.PRIVATE);

        //when
        publicMapLetter.updateRecipientDeleted(true);
        privateMapLetter.updateRecipientDeleted(true);

        //then
        assertThat(publicMapLetter.isRecipientDeleted()).isTrue();
        assertThat(privateMapLetter.isRecipientDeleted()).isTrue();
    }

    @DisplayName("편지와의 거리가 15.00m 이하면 조회에 성공하고, 초과하면 조회에 실패한다.")
    @Test
    void accessByDistanceTest() {
        //given
        MapLetter publicMapLetter = createMapLetter(CreateMapLetterType.PUBLIC);
        MapLetter privateMapLetter = createMapLetter(CreateMapLetterType.PRIVATE);

        //when, then
        assertThatCode(() -> publicMapLetter.validateFindOneMapLetter(MapLetterPolicy.VIEW_DISTANCE, 14.99))
                .doesNotThrowAnyException();
        assertThatCode(() -> privateMapLetter.validateFindOneMapLetter(MapLetterPolicy.VIEW_DISTANCE, 14.99))
                .doesNotThrowAnyException();

        assertThatCode(() -> privateMapLetter.validateFindOneMapLetter(MapLetterPolicy.VIEW_DISTANCE, 15.00))
                .doesNotThrowAnyException();
        assertThatCode(() -> publicMapLetter.validateFindOneMapLetter(MapLetterPolicy.VIEW_DISTANCE, 15.00))
                .doesNotThrowAnyException();

        assertThatThrownBy(() -> publicMapLetter.validateFindOneMapLetter(MapLetterPolicy.VIEW_DISTANCE, 15.1))
                .isInstanceOf(DomainException.class)
                .hasMessage("편지와의 거리가 멀어서 조회가 불가능합니다.");

        assertThatThrownBy(() -> privateMapLetter.validateFindOneMapLetter(MapLetterPolicy.VIEW_DISTANCE, 15.1))
                .isInstanceOf(DomainException.class)
                .hasMessage("편지와의 거리가 멀어서 조회가 불가능합니다.");
    }

    @DisplayName("Private 편지의 경우, 편지를 작성한 유저 혹은 편지의 타겟 유저만 조회에 가능하다.")
    @Test
    void accessPrivateLetterTest() {
        //given
        Long createUserId = 1L;
        Long targetUserId = 2L;
        MapLetter privateMapLetter = createMapLetter(CreateMapLetterType.PRIVATE, createUserId, targetUserId);

        //when, then
        assertThatCode(() -> privateMapLetter.validateAccess(createUserId))
                .doesNotThrowAnyException();
        assertThatCode(() -> privateMapLetter.validateAccess(targetUserId))
                .doesNotThrowAnyException();

        assertThatThrownBy(() -> privateMapLetter.validateAccess(5L))
                .isInstanceOf(CommonForbiddenException.class)
                .hasMessage("편지를 볼 수 있는 권한이 없습니다.");
    }

    @DisplayName("편지를 작성한 유저만 편지를 삭제할 수 있다.")
    @Test
    void shouldAllowOnlyCreatorToDeleteLetter() {
        //given
        Long createUserId = 1L;
        Long targetUserId = 2L;
        MapLetter mapLetter = createMapLetter(CreateMapLetterType.PUBLIC, createUserId, null);
        MapLetter privateMapLetter = createMapLetter(CreateMapLetterType.PRIVATE, createUserId, targetUserId);

        //when, then
        assertThatCode(() -> mapLetter.validDeleteMapLetter(createUserId))
                .doesNotThrowAnyException();
        assertThatCode(() -> privateMapLetter.validDeleteMapLetter(createUserId))
                .doesNotThrowAnyException();

        assertThatThrownBy(() -> mapLetter.validDeleteMapLetter(targetUserId))
                .isInstanceOf(CommonForbiddenException.class)
                .hasMessage("편지를 삭제 할 권한이 없습니다. 편지 삭제에 실패하였습니다.");
        assertThatThrownBy(() -> mapLetter.validDeleteMapLetter(5L))
                .isInstanceOf(CommonForbiddenException.class)
                .hasMessage("편지를 삭제 할 권한이 없습니다. 편지 삭제에 실패하였습니다.");
        assertThatThrownBy(() -> privateMapLetter.validDeleteMapLetter(targetUserId))
                .isInstanceOf(CommonForbiddenException.class)
                .hasMessage("편지를 삭제 할 권한이 없습니다. 편지 삭제에 실패하였습니다.");
    }

    @DisplayName("편지가 블락당하면 편지 삭제에 실패한다.")
    @Test
    void shouldThrowExceptionWhenDeletingBlockedLetter() {
        //given
        Long createUserId = 1L;
        MapLetter mapLetter = MapLetter.builder()
                .id(1L)
                .createUserId(createUserId)
                .targetUserId(1L)
                .isBlocked(true)
                .build();

        //when, then
        assertThatThrownBy(() -> mapLetter.validDeleteMapLetter(createUserId))
                .isInstanceOf(DomainException.class)
                .hasMessage("해당 편지는 신고당한 편지입니다.");
    }

    @DisplayName("특정 편지에 대한 답장들은 편지를 작성한 유저만 확인할 수 있다.")
    @Test
    void replyAccessByCreatorTest() {
        //given
        Long createUserId = 1L;
        Long targetUserId = 2L;
        MapLetter publicMapLetter = createMapLetter(CreateMapLetterType.PUBLIC, createUserId, null);
        MapLetter privateMapLetter = createMapLetter(CreateMapLetterType.PRIVATE, createUserId, targetUserId);

        //when, then
        assertThatCode(() -> publicMapLetter.validFindAllReplyMapLetter(createUserId))
                .doesNotThrowAnyException();
        assertThatCode(() -> privateMapLetter.validFindAllReplyMapLetter(createUserId))
                .doesNotThrowAnyException();

        assertThatThrownBy(() -> privateMapLetter.validFindAllReplyMapLetter(targetUserId))
                .isInstanceOf(CommonForbiddenException.class)
                .hasMessage("편지를 볼 수 있는 권한이 없습니다.");
        assertThatThrownBy(() -> publicMapLetter.validFindAllReplyMapLetter(5L))
                .isInstanceOf(CommonForbiddenException.class)
                .hasMessage("편지를 볼 수 있는 권한이 없습니다.");
        assertThatThrownBy(() -> privateMapLetter.validFindAllReplyMapLetter(5L))
                .isInstanceOf(CommonForbiddenException.class)
                .hasMessage("편지를 볼 수 있는 권한이 없습니다.");
    }

    @DisplayName("Private 편지의 경우, 편지를 저장하지 못한다.")
    @Test
    void archivePrivateFailTest() {
        //given
        MapLetter mapLetter = createMapLetter(CreateMapLetterType.PRIVATE);

        //when, then
        assertThatThrownBy(mapLetter::validMapLetterArchive)
                .isInstanceOf(CommonForbiddenException.class)
                .hasMessage("편지를 저장할 수 있는 권한이 없습니다.");
    }

    @DisplayName("편지가 삭제되었으면 오류가 발생한다.")
    @Test
    void validDeleteTest() {
        //given
        MapLetter publicMapLetter = createMapLetter(CreateMapLetterType.PUBLIC);
        MapLetter privateMapLetter = createMapLetter(CreateMapLetterType.PRIVATE);

        //when
        publicMapLetter.updateDelete(true);
        privateMapLetter.updateDelete(true);

        //then
        assertThatThrownBy(publicMapLetter::validDeleteAndBlocked)
                .isInstanceOf(DomainException.class)
                .hasMessage("해당 편지는 삭제되었습니다.");

        assertThatThrownBy(privateMapLetter::validDeleteAndBlocked)
                .isInstanceOf(DomainException.class)
                .hasMessage("해당 편지는 삭제되었습니다.");
    }

    @DisplayName("편지가 블락되었으면 오류가 발생한다.")
    @Test
    void validBlockTest() {
        //given
        MapLetter privateMapLetter = MapLetter.builder()
                .id(1L)
                .isBlocked(true)
                .type(MapLetterType.PRIVATE)
                .build();

        MapLetter publicMapLetter = MapLetter.builder()
                .id(1L)
                .isBlocked(true)
                .type(MapLetterType.PUBLIC)
                .build();

        //when, then
        assertThatThrownBy(publicMapLetter::validDeleteAndBlocked)
                .isInstanceOf(DomainException.class)
                .hasMessage("해당 편지는 신고당한 편지입니다.");

        assertThatThrownBy(privateMapLetter::validDeleteAndBlocked)
                .isInstanceOf(DomainException.class)
                .hasMessage("해당 편지는 신고당한 편지입니다.");
    }

    @DisplayName("비로그인 유저의 경우, Private 편지에는 접근할 수 없다.")
    @Test
    void anonymousAccessDeniedTest() {
        //given
        MapLetter mapLetter = createMapLetter(CreateMapLetterType.PRIVATE);

        //when, then
        assertThatThrownBy(mapLetter::validatePublicAccess)
                .isInstanceOf(CommonForbiddenException.class)
                .hasMessage("해당 편지에 접근할 수 없습니다.");
    }

    @DisplayName("타겟 유저 ID가 일치하면 true를 반환한다.")
    @Test
    void isTargetUserCheckTest() {
        //given
        Long targetUserId = 2L;
        MapLetter mapLetter = createMapLetter(CreateMapLetterType.PRIVATE, 1L, targetUserId);

        //when, then
        assertThat(mapLetter.isTargetUser(targetUserId)).isTrue();
    }

    @DisplayName("타겟 유저만 받은 편지를 삭제할 수 있다.")
    @Test
    void validateRecipientDeletionTest() {
        //given
        Long targetUserId = 2L;
        MapLetter mapLetter = createMapLetter(CreateMapLetterType.PRIVATE, 1L, targetUserId);

        //when, then
        assertThatCode(() -> mapLetter.validateRecipientDeletion(targetUserId))
                .doesNotThrowAnyException();

        assertThatThrownBy(() -> mapLetter.validateRecipientDeletion(1L))
                .isInstanceOf(CommonForbiddenException.class)
                .hasMessage("편지를 삭제 할 권한이 없습니다. 편지 삭제에 실패하였습니다.");
    }

    @DisplayName("받은 편지가 이미 삭제되었을 경우, 삭제에 실패한다.")
    @Test
    void shouldThrowExceptionWhenDeletingAlreadyDeletedReceivedLetterTest() {
        //given
        Long targetUserId = 2L;
        MapLetter mapLetter = MapLetter.builder()
                .id(1L)
                .targetUserId(targetUserId)
                .isRecipientDeleted(true)
                .build();

        //when, then
        assertThatThrownBy(() -> mapLetter.validateRecipientDeletion(targetUserId))
                .isInstanceOf(DomainException.class)
                .hasMessage("해당 편지는 이미 삭제되었습니다.");
    }

    @DisplayName("받은 편지가 이미 삭제되었을 경우, 삭제에 실패한다.")
    @Test
    void shouldThrowExceptionWhenDeletingAlreadyBlockedReceivedLetterTest() {
        //given
        Long targetUserId = 2L;
        MapLetter mapLetter = MapLetter.builder()
                .id(1L)
                .targetUserId(targetUserId)
                .isBlocked(true)
                .build();

        //when, then
        assertThatThrownBy(() -> mapLetter.validateRecipientDeletion(targetUserId))
                .isInstanceOf(DomainException.class)
                .hasMessage("해당 편지는 신고당한 편지입니다.");
    }

    @DisplayName("편지를 작성한 유저 ID가 일치하면 true를 반환한다.")
    @Test
    void isCreatorCheckTest() {
        //given
        Long createUserId = 1L;
        MapLetter mapLetter = createMapLetter(CreateMapLetterType.PUBLIC, createUserId, null);

        //when, then
        assertThat(mapLetter.isCreated(createUserId)).isTrue();
    }

    private MapLetter createMapLetter(CreateMapLetterType type, Long createUserId, Long targetUserId) {
        return MapLetter.builder()
                .id(1L)
                .title("title")
                .content("content")
                .latitude(new BigDecimal("37.5665"))
                .longitude(new BigDecimal("126.9780"))
                .font("font")
                .paper("paper")
                .label("label")
                .description("description")
                .type(type.equals(CreateMapLetterType.PUBLIC) ? MapLetterType.PUBLIC : MapLetterType.PRIVATE)
                .targetUserId(targetUserId)
                .createUserId(createUserId)
                .createdAt(LocalDateTime.of(2025, 7, 11, 17, 20))
                .updatedAt(LocalDateTime.of(2025, 7, 11, 17, 20))
                .isDeleted(false)
                .isBlocked(false)
                .isRead(false)
                .isRecipientDeleted(false)
                .build();
    }

    private MapLetter createMapLetter(CreateMapLetterType type) {
        return createMapLetter(type, 1L, 2L);
    }

    enum CreateMapLetterType {
        PRIVATE,
        PUBLIC
    }
}
