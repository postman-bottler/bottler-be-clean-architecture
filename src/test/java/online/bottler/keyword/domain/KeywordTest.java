package online.bottler.keyword.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import online.bottler.letter.domain.Keyword;

class KeywordTest {

    @Test
    @DisplayName("Keyword 생성 - 모든 필드가 올바르게 설정되어야 한다.")
    void createKeyword() {
        // given
        Long id = 1L;
        String keyword = "Spring";
        String category = "Technology";

        // when
        Keyword keywordObj = Keyword.of(id, keyword, category);

        // then
        assertThat(keywordObj).isNotNull();
        assertThat(keywordObj.getId()).isEqualTo(id);
        assertThat(keywordObj.getKeyword()).isEqualTo(keyword);
        assertThat(keywordObj.getCategory()).isEqualTo(category);
    }
}
