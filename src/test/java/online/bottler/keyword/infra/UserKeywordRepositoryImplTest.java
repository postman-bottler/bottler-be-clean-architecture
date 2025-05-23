package online.bottler.keyword.infra;

import static org.assertj.core.api.Assertions.assertThat;
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
import online.bottler.letter.domain.UserKeyword;
import online.bottler.letter.adapter.out.persistence.entity.UserKeywordEntity;
import online.bottler.letter.adapter.out.persistence.repository.UserKeywordJdbcRepository;
import online.bottler.letter.adapter.out.persistence.repository.UserKeywordJpaRepository;
import online.bottler.letter.adapter.out.persistence.UserKeywordRepositoryImpl;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class UserKeywordRepositoryImplTest extends TestBase {

    @InjectMocks
    private UserKeywordRepositoryImpl repository;

    @Mock
    private UserKeywordJpaRepository jpaRepository;

    @Mock
    private UserKeywordJdbcRepository jdbcRepository;

    private List<UserKeywordEntity> mockUserKeywordEntities;
    private List<UserKeyword> mockUserKeywords;

    @BeforeEach
    void setup() {
        mockUserKeywordEntities = List.of(
                UserKeywordEntity.builder().userId(1L).keyword("keyword1").build(),
                UserKeywordEntity.builder().userId(1L).keyword("keyword2").build()
        );

        mockUserKeywords = List.of(
                UserKeyword.builder().userId(1L).keyword("keyword1").build(),
                UserKeyword.builder().userId(1L).keyword("keyword2").build()
        );
    }

    @Test
    @DisplayName("특정 userId로 모든 키워드 조회")
    void findUserKeywordsByUserId() {
        // given
        when(jpaRepository.findAllByUserId(1L)).thenReturn(mockUserKeywordEntities);

        // when
        List<UserKeyword> result = repository.findUserKeywordsByUserId(1L);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getKeyword()).isEqualTo("keyword1");
        assertThat(result.get(1).getKeyword()).isEqualTo("keyword2");
        verify(jpaRepository, times(1)).findAllByUserId(1L);
    }

    @Test
    @DisplayName("특정 userId로 키워드 문자열 조회")
    void findKeywordsByUserId() {
        // given
        when(jpaRepository.findKeywordsByUserId(1L)).thenReturn(List.of("keyword1", "keyword2"));

        // when
        List<String> result = repository.findKeywordsByUserId(1L);

        // then
        assertThat(result).containsExactlyInAnyOrder("keyword1", "keyword2");
        verify(jpaRepository, times(1)).findKeywordsByUserId(1L);
    }

    @Test
    @DisplayName("특정 userId로 키워드 전부 교체")
    void replaceKeywordsByUserId() {
        // given
        Long userId = 1L;

        // when
        repository.replaceKeywordsByUserId(mockUserKeywords, userId);

        // then
        verify(jdbcRepository, times(1)).deleteAllByUserId(userId);
        verify(jdbcRepository, times(1)).batchInsertKeywords(mockUserKeywords);
    }
}
