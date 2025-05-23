package online.bottler.letter.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import online.bottler.letter.application.LetterBoxService;
import online.bottler.letter.application.LetterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import online.bottler.TestBase;
import online.bottler.letter.application.command.LetterBoxCommand;
import online.bottler.letter.adapter.in.web.request.LetterRequest;
import online.bottler.letter.application.port.out.LetterRepository;
import online.bottler.letter.domain.Letter;
import online.bottler.letter.exception.LetterAuthorMismatchException;
import online.bottler.letter.exception.LetterNotFoundException;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class LetterServiceTest extends TestBase {

    @InjectMocks
    private LetterService letterService;

    @Mock
    private LetterRepository letterRepository;

    @Mock
    private LetterBoxService letterBoxService;

    private Letter mockLetter;

    @BeforeEach
    void setUp() {
        /*mockLetter = Letter.builder()
                .id(1L)
                .title("테스트 제목")
                .content("테스트 내용")
                .userId(100L)
                .build();*/
    }

    @Test
    @DisplayName("새로운 편지 생성")
    void createLetter() {
        // given
        LetterRequest requestDTO = new LetterRequest("테스트 제목", "테스트 내용", List.of("키워드1"), "폰트", "편지지", "라벨");
        Long userId = 100L;

        when(letterRepository.save(any(Letter.class))).thenReturn(mockLetter);
        doNothing().when(letterBoxService).saveLetter(any(LetterBoxCommand.class));

        // when
//        Letter result = letterService.createLetter(requestDTO, userId);

        // then
//        assertThat(result).isNotNull();
//        assertThat(result.getId()).isEqualTo(1L);
        verify(letterRepository, times(1)).save(any(Letter.class));
        verify(letterBoxService, times(1)).saveLetter(any(LetterBoxCommand.class));
    }

    @Nested
    @DisplayName("편지 조회 테스트")
    class FindLetterTests {

        @Test
        @DisplayName("편지 조회 - 존재하는 경우")
        void findLetter() {
            // given
            when(letterRepository.findById(1L)).thenReturn(Optional.of(mockLetter));

            // when
            Letter result = letterService.findLetter(1L);

            // then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            verify(letterRepository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("편지 조회 - 존재하지 않는 경우 예외 발생")
        void findLetterNotFound() {
            // given
            when(letterRepository.findById(1L)).thenReturn(Optional.empty());

            // when & then
            assertThrows(LetterNotFoundException.class, () -> letterService.findLetter(1L));
            verify(letterRepository, times(1)).findById(1L);
        }
    }

    @Test
    @DisplayName("추천 편지 목록 조회")
    void findRecommendedLetters() {
        // given
        List<Long> letterIds = List.of(1L, 2L);
        List<Letter> mockLetters = List.of(
//                Letter.builder().id(1L).title("추천 제목1").content("내용1").build(),
//                Letter.builder().id(2L).title("추천 제목2").content("내용2").build()
        );
        when(letterRepository.findAllByIds(letterIds)).thenReturn(mockLetters);

        // when
//        List<Letter> result = letterService.findRecommendedLetters(letterIds);

        // then
//        assertThat(result).hasSize(2);
//        assertThat(result.get(0).getId()).isEqualTo(1L);
        verify(letterRepository, times(1)).findAllByIds(letterIds);
    }

    @Nested
    @DisplayName("편지 삭제")
    class DeleteLetterTests {

        @Test
        @DisplayName("작성자 불일치로 편지 삭제 실패")
        void deleteLettersMismatchAuthor() {
            // given
            List<Long> letterIds = List.of(1L, 2L);
            when(letterRepository.findById(1L)).thenReturn(Optional.of(mockLetter));

            // when & then
            assertThrows(LetterAuthorMismatchException.class,
                    () -> letterService.softDeleteLetters(letterIds, 200L));
            verify(letterRepository, times(1)).findById(1L);
        }

        @Test
        @DisplayName("편지 삭제 성공")
        void deleteLetters() {
            // given
            List<Long> letterIds = List.of(1L);
            when(letterRepository.findById(1L)).thenReturn(Optional.of(mockLetter));
            doNothing().when(letterRepository).softDeleteByIds(anyList());

            // when
            letterService.softDeleteLetters(letterIds, 100L);

            // then
            verify(letterRepository, times(1)).softDeleteByIds(letterIds);
        }
    }

    @Test
    @DisplayName("편지 차단")
    void softBlockLetter() {
        // given
        when(letterRepository.findById(1L)).thenReturn(Optional.of(mockLetter));
        doNothing().when(letterRepository).softBlockById(1L);

        // when
        Long result = letterService.softBlockLetter(1L);

        // then
        assertThat(result).isEqualTo(100L);
        verify(letterRepository, times(1)).softBlockById(1L);
    }

    @Test
    @DisplayName("편지 존재 여부 확인")
    void existsLetterById() {
        // given
        when(letterRepository.existsById(1L)).thenReturn(true);

        // when
        boolean result = letterService.existsLetterById(1L);

        // then
        assertThat(result).isTrue();
        verify(letterRepository, times(1)).existsById(1L);
    }
}
