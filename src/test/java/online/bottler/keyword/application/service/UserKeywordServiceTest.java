package online.bottler.keyword.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
import online.bottler.letter.adapter.in.web.request.UserKeywordRequest;
import online.bottler.letter.application.response.UserKeywordResponse;
import online.bottler.letter.application.port.out.UserKeywordRepository;
import online.bottler.letter.domain.UserKeyword;
import online.bottler.letter.application.UserKeywordService;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class UserKeywordServiceTest extends TestBase {

    @InjectMocks
    private UserKeywordService userKeywordService;

    @Mock
    private UserKeywordRepository userKeywordRepository;

    @Test
    @DisplayName("성공적으로 사용자 키워드를 조회한다")
    void findUserKeywords() {
        // given
        Long userId = 1L;
        List<UserKeyword> mockKeywords = List.of(
//                UserKeyword.builder().id(1L).userId(userId).keyword("키워드1").build(),
//                UserKeyword.builder().id(2L).userId(userId).keyword("키워드2").build()
        );

        when(userKeywordRepository.findUserKeywordsByUserId(userId)).thenReturn(mockKeywords);

        // when
        UserKeywordResponse result = userKeywordService.findUserKeywords(userId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.keywords()).hasSize(2);
        assertThat(result.keywords()).containsExactly("키워드1", "키워드2");
        verify(userKeywordRepository, times(1)).findUserKeywordsByUserId(userId);
    }

    @Test
    @DisplayName("성공적으로 사용자 키워드를 생성한다")
    void createKeywords() {
        // given
        Long userId = 1L;
        UserKeywordRequest requestDTO = new UserKeywordRequest(List.of("키워드1", "키워드2"));

        // when
        userKeywordService.createKeywords(requestDTO, userId);

        // then
        verify(userKeywordRepository, times(1)).replaceKeywordsByUserId(any(), anyLong());
    }

    @Test
    @DisplayName("성공적으로 사용자 ID로 키워드를 조회한다")
    void findKeywords() {
        // given
        Long userId = 1L;
        List<String> mockKeywords = List.of("키워드1", "키워드2");

        when(userKeywordRepository.findKeywordsByUserId(userId)).thenReturn(mockKeywords);

        // when
        List<String> result = userKeywordService.findKeywords(userId);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly("키워드1", "키워드2");
        verify(userKeywordRepository, times(1)).findKeywordsByUserId(userId);
    }
}
