package online.bottler.keyword.infra;

import static org.assertj.core.api.Assertions.assertThat;
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
import online.bottler.letter.domain.Keyword;
import online.bottler.letter.infra.entity.KeywordEntity;
import online.bottler.letter.infra.KeywordJpaRepository;
import online.bottler.letter.infra.KeywordRepositoryImpl;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@DisplayName("KeywordRepositoryImpl 테스트")
class KeywordRepositoryImplTest extends TestBase {

    @InjectMocks
    private KeywordRepositoryImpl keywordRepository;

    @Mock
    private KeywordJpaRepository keywordJpaRepository;

    private KeywordEntity keywordEntity;

    @BeforeEach
    @DisplayName("테스트 데이터 초기화")
    void setUp() {
        keywordEntity = KeywordEntity.builder()
                .id(1L)
                .keyword("Inspiration")
                .category("Motivation")
                .build();
    }

    @Test
    @DisplayName("키워드 목록 조회")
    void getKeywords() {
        // given
        when(keywordJpaRepository.findAll()).thenReturn(List.of(keywordEntity));

        // when
        List<Keyword> keywords = keywordRepository.getKeywords();

        // then
        assertThat(keywords).isNotEmpty();
        assertThat(keywords.get(0).getId()).isEqualTo(1L);
        assertThat(keywords.get(0).getKeyword()).isEqualTo("Inspiration");
        assertThat(keywords.get(0).getCategory()).isEqualTo("Motivation");
    }
}
