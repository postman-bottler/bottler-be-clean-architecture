package online.bottler.mapletter.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import online.bottler.global.exception.CommonForbiddenException;
import online.bottler.global.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReplyMapLetterTest {

    @DisplayName("편지 삭제에 성공한다.")
    @Test
    void deleteTest() {
        //given
        ReplyMapLetter replyMapLetter = createReplyMapLetter();

        //when
        replyMapLetter.updateDelete(true);

        //then
        assertThat(replyMapLetter.isDeleted()).isTrue();
    }

    @DisplayName("받은 편지 삭제에 성공한다.")
    @Test
    void blockTest() {
        //given
        ReplyMapLetter replyMapLetter = createReplyMapLetter();

        //when
        replyMapLetter.updateRecipientDeleted(true);

        //then
        assertThat(replyMapLetter.isRecipientDeleted()).isTrue();
    }

    @DisplayName("답장을 작성한 유저, 원본 편지를 작성 한 유저만 답장 편지 조회에 성공한다.")
    @Test
    void canAccessReplyLetterTest() {
        //given
        Long sourceLetterCreateUserId = 1L;
        Long replyLetterCreateUserId = 2L;
        MapLetter sourceLetter = MapLetter.builder()
                .createUserId(sourceLetterCreateUserId)
                .build();

        ReplyMapLetter replyMapLetter = createReplyMapLetter(replyLetterCreateUserId);

        //when, then
        assertThatCode(() -> replyMapLetter.validFindOneReplyMapLetter(replyLetterCreateUserId, sourceLetter))
                .doesNotThrowAnyException();
        assertThatCode(() -> replyMapLetter.validFindOneReplyMapLetter(sourceLetterCreateUserId, sourceLetter))
                .doesNotThrowAnyException();

        assertThatThrownBy(() -> replyMapLetter.validFindOneReplyMapLetter(5L, sourceLetter))
                .isInstanceOf(CommonForbiddenException.class)
                .hasMessage("편지를 볼 수 있는 권한이 없습니다.");
    }

    @DisplayName("답장이 삭제되면 권한이 있어도 조회할 수 없다.")
    @Test
    void cannotViewDeletedReplyTest() {
        //given
        Long sourceLetterCreateUserId = 1L;
        Long replyLetterCreateUserId = 2L;
        MapLetter sourceLetter = MapLetter.builder()
                .createUserId(sourceLetterCreateUserId)
                .build();

        ReplyMapLetter replyMapLetter = createDeletedReplyMapLetter(replyLetterCreateUserId);

        //when, then
        assertThatThrownBy(() -> replyMapLetter.validFindOneReplyMapLetter(replyLetterCreateUserId, sourceLetter))
                .isInstanceOf(DomainException.class)
                .hasMessage("해당 편지는 삭제되었습니다.");

        assertThatThrownBy(() -> replyMapLetter.validFindOneReplyMapLetter(sourceLetterCreateUserId, sourceLetter))
                .isInstanceOf(DomainException.class)
                .hasMessage("해당 편지는 삭제되었습니다.");
    }

    @DisplayName("블락된 답장은 권한이 있어도 조회할 수 없다.")
    @Test
    void cannotViewBlockedReplyTest() {
        //given
        Long sourceLetterCreateUserId = 1L;
        Long replyLetterCreateUserId = 2L;
        MapLetter sourceLetter = MapLetter.builder()
                .createUserId(sourceLetterCreateUserId)
                .build();

        ReplyMapLetter replyMapLetter = createBlockedReplyMapLetter(replyLetterCreateUserId);

        //when, then
        assertThatThrownBy(() -> replyMapLetter.validFindOneReplyMapLetter(sourceLetterCreateUserId, sourceLetter))
                .isInstanceOf(DomainException.class)
                .hasMessage("해당 편지는 신고당한 편지입니다.");

        assertThatThrownBy(() -> replyMapLetter.validFindOneReplyMapLetter(replyLetterCreateUserId, sourceLetter))
                .isInstanceOf(DomainException.class)
                .hasMessage("해당 편지는 신고당한 편지입니다.");
    }

    @DisplayName("답장 편지를 작성 한 유저만 편지 삭제에 성공한다.")
    @Test
    void validDeleteReplyMapLetterTest() {
        //given
        Long createUserId = 1L;
        ReplyMapLetter replyMapLetter = createReplyMapLetter(createUserId);

        //when, then
        assertThatCode(() -> replyMapLetter.validDeleteReplyMapLetter(createUserId))
                .doesNotThrowAnyException();

        assertThatThrownBy(() -> replyMapLetter.validDeleteReplyMapLetter(5L))
                .isInstanceOf(CommonForbiddenException.class)
                .hasMessage("편지를 삭제 할 권한이 없습니다. 편지 삭제에 실패하였습니다.");
    }

    @DisplayName("삭제된 답장은 권한이 있어도 삭제할 수 없다.")
    @Test
    void cannotDeleteDeletedReplyTest() {
        //given
        Long createUserId = 1L;
        ReplyMapLetter replyMapLetter = createDeletedReplyMapLetter(createUserId);

        //when, then
        assertThatThrownBy(() -> replyMapLetter.validDeleteReplyMapLetter(createUserId))
                .isInstanceOf(DomainException.class)
                .hasMessage("해당 편지는 삭제되었습니다.");

        assertThatThrownBy(() -> replyMapLetter.validDeleteReplyMapLetter(createUserId))
                .isInstanceOf(DomainException.class)
                .hasMessage("해당 편지는 삭제되었습니다.");
    }

    @DisplayName("블락된 답장은 권한이 있어도 삭제할 수 없다.")
    @Test
    void cannotDeleteBlockedReplyTest() {
        //given
        Long createUserId = 1L;
        ReplyMapLetter replyMapLetter = createBlockedReplyMapLetter(createUserId);

        //when, then
        assertThatThrownBy(() -> replyMapLetter.validDeleteReplyMapLetter(createUserId))
                .isInstanceOf(DomainException.class)
                .hasMessage("해당 편지는 신고당한 편지입니다.");

        assertThatThrownBy(() -> replyMapLetter.validDeleteReplyMapLetter(createUserId))
                .isInstanceOf(DomainException.class)
                .hasMessage("해당 편지는 신고당한 편지입니다.");
    }

    @DisplayName("답장은 원본 편지를 작성한 유저만 마이페이지에서 편지 삭제에 성공한다.")
    @Test
    void replyDeletePermissionTest() {
        //given
        Long sourceLetterCreateUserId = 2L;
        ReplyMapLetter replyMapLetter = createReplyMapLetter();

        //when, then
        assertThatCode(()->replyMapLetter.validateRecipientDeletion(sourceLetterCreateUserId, sourceLetterCreateUserId))
                .doesNotThrowAnyException();

        assertThatThrownBy(() -> replyMapLetter.validateRecipientDeletion(5L, sourceLetterCreateUserId))
                .isInstanceOf(CommonForbiddenException.class)
                .hasMessage("편지를 삭제 할 권한이 없습니다. 편지 삭제에 실패하였습니다.");
    }

    @DisplayName("삭제된 답장은 권한이 있어도 마이페이지에서 삭제할 수 없다.")
    @Test
    void cannotRecipientDeleteDeletedReplyTest() {
        //given
        Long sourceLetterCreateUserId = 2L;
        ReplyMapLetter replyMapLetter = ReplyMapLetter.builder()
                .isRecipientDeleted(true)
                .build();

        //when, then
        assertThatThrownBy(() -> replyMapLetter.validateRecipientDeletion(sourceLetterCreateUserId, sourceLetterCreateUserId))
                .isInstanceOf(DomainException.class)
                .hasMessage("해당 편지는 이미 삭제되었습니다.");
    }

    @DisplayName("블락된 답장은 권한이 있어도 마이페이지에서 삭제할 수 없다.")
    @Test
    void cannotRecipientDeleteBlockedReplyTest() {
        //given
        Long createUserId = 1L;
        ReplyMapLetter replyMapLetter = createBlockedReplyMapLetter(createUserId);

        //when, then
        assertThatThrownBy(() -> replyMapLetter.validDeleteReplyMapLetter(createUserId))
                .isInstanceOf(DomainException.class)
                .hasMessage("해당 편지는 신고당한 편지입니다.");

        assertThatThrownBy(() -> replyMapLetter.validDeleteReplyMapLetter(createUserId))
                .isInstanceOf(DomainException.class)
                .hasMessage("해당 편지는 신고당한 편지입니다.");
    }

    private ReplyMapLetter createReplyMapLetter(Long createUserId, boolean isBlocked, boolean isDeleted) {
        return ReplyMapLetter.builder()
                .replyLetterId(1L)
                .sourceLetterId(2L)
                .font("font")
                .paper("paper")
                .label("label")
                .content("content")
                .isBlocked(isBlocked)
                .isDeleted(isDeleted)
                .createUserId(createUserId)
                .createdAt(LocalDateTime.of(2025, 7, 11, 19, 10, 0))
                .updatedAt(LocalDateTime.of(2025, 7, 11, 19, 10, 0))
                .isRecipientDeleted(false)
                .build();
    }

    private ReplyMapLetter createDeletedReplyMapLetter(){
        return createReplyMapLetter(1L, false, true);
    }

    private ReplyMapLetter createBlockedReplyMapLetter(){
        return createReplyMapLetter(1L, true, false);
    }

    private ReplyMapLetter createDeletedReplyMapLetter(Long createUserId){
        return createReplyMapLetter(createUserId, false, true);
    }

    private ReplyMapLetter createBlockedReplyMapLetter(Long createUserId){
        return createReplyMapLetter(createUserId, true, false);
    }

    private ReplyMapLetter createReplyMapLetter(Long createUserId) {
        return createReplyMapLetter(createUserId, false, false);
    }

    private ReplyMapLetter createReplyMapLetter() {
        return createReplyMapLetter(1L, false, false);
    }
}