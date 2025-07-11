package online.bottler.mapletter.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import online.bottler.global.exception.CommonForbiddenException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MapLetterArchiveTest {

    @DisplayName("편지를 보관한 유저가 아니면 편지 보관 취소에 실패한다.")
    @Test
    void validDeleteArchivedLetterTest() {
        //given
        Long userId = 1L;
        MapLetterArchive mapLetterArchive = createMapLetterArchive(userId);

        //when, then
        assertThatCode(() -> mapLetterArchive.validDeleteArchivedLetter(userId))
                .doesNotThrowAnyException();

        assertThatThrownBy(() -> mapLetterArchive.validDeleteArchivedLetter(5L))
                .isInstanceOf(CommonForbiddenException.class)
                .hasMessage("편지 보관 취소를 할 권한이 없습니다. 편지 보관 취소에 실패했습니다.");
    }

    private MapLetterArchive createMapLetterArchive(Long userId) {
        return MapLetterArchive.builder()
                .mapLetterArchiveId(1L)
                .mapLetterId(1L)
                .userId(userId)
                .createdAt(LocalDateTime.of(2025, 7, 11, 19, 0, 0))
                .build();
    }
}
