package online.bottler.keyword.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import online.bottler.TestBase;
import online.bottler.letter.application.dto.response.KeywordResponseDTO;
import online.bottler.letter.application.dto.response.KeywordResponseDTO.CategoryKeywordsDTO;
import online.bottler.letter.application.port.out.KeywordRepository;
import online.bottler.letter.domain.Keyword;
import online.bottler.letter.application.KeywordService;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class KeywordServiceTest extends TestBase {

    @InjectMocks
    private KeywordService keywordService;

    @Mock
    private KeywordRepository keywordRepository;

    @Test
    @DisplayName("카테고리별 키워드 목록을 정확히 반환한다")
    void getKeywords() {
        // given
        List<Keyword> mockKeywords = List.of(
                Keyword.of(1L, "사랑", "감정"),
                Keyword.of(2L, "행복", "감정"),
                Keyword.of(3L, "우정", "사회"),
                Keyword.of(4L, "성공", "자기계발")
        );

        when(keywordRepository.getKeywords()).thenReturn(mockKeywords);

        // when
        KeywordResponseDTO result = keywordService.getKeywords();

        // then
        assertThat(result).isNotNull();
        assertThat(result.categories()).hasSize(3);

        // 감정 카테고리 검증
        CategoryKeywordsDTO emotionCategory = findCategory(result, "감정");
        assertThat(emotionCategory.keywords()).containsExactly("사랑", "행복");

        // 사회 카테고리 검증
        CategoryKeywordsDTO socialCategory = findCategory(result, "사회");
        assertThat(socialCategory.keywords()).containsExactly("우정");

        // 자기계발 카테고리 검증
        CategoryKeywordsDTO selfDevelopmentCategory = findCategory(result, "자기계발");
        assertThat(selfDevelopmentCategory.keywords()).containsExactly("성공");

        verify(keywordRepository, times(1)).getKeywords();
    }

    private CategoryKeywordsDTO findCategory(KeywordResponseDTO result, String category) {
        return result.categories().stream()
                .filter(c -> c.category().equals(category))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + category));
    }
}
