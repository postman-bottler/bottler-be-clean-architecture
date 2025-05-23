package online.bottler.keyword.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import online.bottler.letter.domain.LetterKeyword;

class LetterKeywordTest {

    @Test
    @DisplayName("from 메서드로 LetterKeyword 생성 검증")
    void of() {
        // given
        Long letterId = 102L;
        String keyword = "Motivation";

        // when
        LetterKeyword letterKeyword = LetterKeyword.create(letterId, keyword);

        // then
        assertThat(letterKeyword).isNotNull();
        assertThat(letterKeyword.getId()).isNull(); // ID는 초기값으로 null
        assertThat(letterKeyword.getLetterId()).isEqualTo(letterId);
        assertThat(letterKeyword.getKeyword()).isEqualTo(keyword);
        assertThat(letterKeyword.isDeleted()).isFalse(); // 초기값은 false
    }

    @Test
    @DisplayName("LetterKeyword 생성 - 모든 필드가 올바르게 설정되어야 한다.")
    void createLetterKeyword() {
        // given
        Long id = 1L;
        Long letterId = 102L;
        String keyword = "Motivation";

        // when
        LetterKeyword letterKeyword = LetterKeyword.of(id, letterId, keyword, true);

        // then
        assertThat(letterKeyword).isNotNull();
        assertThat(letterKeyword.getId()).isEqualTo(id);
        assertThat(letterKeyword.getLetterId()).isEqualTo(letterId);
        assertThat(letterKeyword.getKeyword()).isEqualTo(keyword);
        assertThat(letterKeyword.isDeleted()).isTrue(); // 빌더로 설정한 값
    }
}
