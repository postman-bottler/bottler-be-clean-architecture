package online.bottler.keyword.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import online.bottler.TestBase;
import online.bottler.letter.application.response.FrequentKeywordsDTO;
import online.bottler.letter.application.port.out.LetterKeywordRepository;
import online.bottler.letter.domain.LetterKeyword;
import online.bottler.letter.application.LetterKeywordService;
import online.bottler.letter.application.LetterService;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class LetterKeywordServiceTest extends TestBase {

    @InjectMocks
    private LetterKeywordService letterKeywordService;

    @Mock
    private LetterKeywordRepository letterKeywordRepository;

    @Mock
    private LetterService letterService;

    @BeforeEach
    void setUp() {
        // 기본 Mock 설정
    }

    @Test
    @DisplayName("편지 키워드 생성")
    void createLetterKeywords() {
        // given
        Long letterId = 1L;
        List<String> keywords = List.of("사랑", "우정", "행복");
        List<LetterKeyword> expectedKeywords = keywords.stream()
                .map(keyword -> LetterKeyword.create(letterId, keyword))
                .toList();

        when(letterKeywordRepository.saveAll(anyList())).thenReturn(expectedKeywords);

        // when
        List<LetterKeyword> result = letterKeywordService.createLetterKeywords(letterId, keywords);

        // then
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getKeyword()).isEqualTo("사랑");
        verify(letterKeywordRepository, times(1)).saveAll(anyList());
    }

    @Test
    @DisplayName("편지 ID로 키워드 조회")
    void getKeywords() {
        // given
        Long letterId = 1L;
        List<LetterKeyword> mockKeywords = List.of(
                LetterKeyword.create(letterId, "사랑"),
                LetterKeyword.create(letterId, "행복")
        );

        when(letterKeywordRepository.getKeywordsByLetterId(letterId)).thenReturn(mockKeywords);

        // when
        List<LetterKeyword> result = letterKeywordService.getKeywords(letterId);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getKeyword()).isEqualTo("사랑");
        verify(letterKeywordRepository, times(1)).getKeywordsByLetterId(letterId);
    }

    @Test
    @DisplayName("키워드를 삭제 상태로 마킹")
    void markKeywordsAsDeleted() {
        // given
        List<Long> letterIds = List.of(1L, 2L, 3L);

        doNothing().when(letterKeywordRepository).markKeywordsAsDeleted(anyList());

        // when
        letterKeywordService.markKeywordsAsDeleted(letterIds);

        // then
        verify(letterKeywordRepository, times(1)).markKeywordsAsDeleted(letterIds);
    }

    @Test
    @DisplayName("자주 사용된 키워드 조회")
    void getTopFrequentKeywords() {
        // given
        Long userId = 1L;
        List<Long> letterIds = List.of(1L, 2L, 3L);
        List<String> mockFrequentKeywords = List.of(
                "사랑",
                "행복",
                "우정"
        );

        when(letterService.findIdsByUserId(userId)).thenReturn(letterIds);
        when(letterKeywordRepository.getFrequentKeywords(letterIds)).thenReturn(mockFrequentKeywords);

        // when
        FrequentKeywordsDTO result = letterKeywordService.getTopFrequentKeywords(userId);

        // then
        assertThat(result.keywords()).containsExactly("사랑", "행복", "우정");
        verify(letterService, times(1)).findIdsByUserId(userId);
        verify(letterKeywordRepository, times(1)).getFrequentKeywords(letterIds);
    }
}
