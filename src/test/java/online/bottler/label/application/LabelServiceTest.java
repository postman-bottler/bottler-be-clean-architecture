package online.bottler.label.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import online.bottler.label.application.repository.LabelRepository;
import online.bottler.label.domain.Label;
import online.bottler.label.application.dto.LabelResponseDTO;
import online.bottler.label.domain.LabelType;
import online.bottler.label.exception.FirstComeFirstServedLabelException;
import online.bottler.user.application.repository.UserRepository;
import online.bottler.user.application.UserService;
import online.bottler.user.domain.Provider;
import online.bottler.user.domain.Role;
import online.bottler.user.domain.User;
import online.bottler.user.infra.UserJpaRepository;

@ExtendWith(MockitoExtension.class)
class LabelServiceTest {
    @Mock
    private LabelRepository labelRepository;

    @Mock
    private UserJpaRepository userJpaRepository;

    @InjectMocks
    private LabelService labelService;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        reset(labelRepository, userJpaRepository);
    }

    @Test
    @DisplayName("주어진 이미지 URL로 라벨을 생성해서 저장한다.")
    void createLabel() {
        // given
        String imageUrl = "http://example.com/image.png";

        // when
        labelService.createLabel(imageUrl, 10);

        // then
        verify(labelRepository, times(1)).save(any(Label.class));
    }

    @Test
    @DisplayName("저장된 모든 라벨이 반환된다.")
    void findAllLabels() {
        // given
        Label label1 = Label.createLabel("http://example.com/image1.png", 10);
        Label label2 = Label.createLabel("http://example.com/image2.png", 10);
        List<Label> labels = List.of(label1, label2);

        when(labelRepository.findAllLabels()).thenReturn(labels);

        // when
        List<LabelResponseDTO> dtos = labelService.findAllLabels();

        // then
        assertNotNull(dtos, "Response DTO 리스트는 null이 될 수 없음");
        assertEquals(2, dtos.size(), "DTO 리스트 사이즈는 2");

        verify(labelRepository, times(1)).findAllLabels();
    }

    @Test
    @DisplayName("특정 유저가 소유한 라벨 리스트를 반환한다.")
    void findUserLabels() {
        // given
        Long userId = 1L;
        Label label1 = Label.createLabel("http://example.com/image1.png", 10);
        Label label2 = Label.createLabel("http://example.com/image2.png", 10);
        List<Label> labels = List.of(label1, label2);

        when(labelRepository.findLabelsByUser(userId)).thenReturn(labels);

        // when
        List<LabelResponseDTO> dtos = labelService.findUserLabels(userId);

        // then
        assertNotNull(dtos, "Response DTO 리스트는 null이 될 수 없음");
        assertEquals(2, dtos.size(), "DTO 리스트 사이즈는 2");

        verify(labelRepository, times(1)).findLabelsByUser(userId);
    }

    @Test
    @DisplayName("라벨 타입이 FIRST_COME인 라벨만 조회한다.")
    void findFirstComeLabels() {
        // given
        Label label1 = Label.createLabel("imageUrl1", 10);
        Label label2 = Label.createLabel("imageUrl2", 20);

        when(labelRepository.findFirstComeLabels()).thenReturn(List.of(label1, label2));

        // when
        List<LabelResponseDTO> response = labelService.findFirstComeLabels();

        // then
        assertNotNull(response, "findFirstComeLabels의 반환값은 null이 아님");
        assertEquals(2, response.size(), "FIRST_COME 라벨 개수는 2개임");

        verify(labelRepository, times(1)).findFirstComeLabels();
    }

    @Test
    @DisplayName("사용자가 선착순 라벨을 정상적으로 뽑는다.")
    void createFirstComeFirstServedLabel_success() {
        // given
        Long userId = 1L;
        User user = User.createUser(1L, "email", "password", "nickname", "imageUrl", Role.USER, Provider.LOCAL,
                LocalDateTime.now(), LocalDateTime.now(), false, 0);
        Label label = mock(Label.class);

        when(userService.findById(userId)).thenReturn(user);
        when(labelRepository.findByLabelType(LabelType.FIRST_COME)).thenReturn(List.of(label));
        when(labelRepository.existsUserLabelByUserAndLabel(user, label)).thenReturn(false);
        when(label.isOwnedCountValid()).thenReturn(true);

        // when
        labelService.createFirstComeFirstServedLabel(userId);

        // then
        verify(labelRepository, times(1)).updateOwnedCount(label);
        verify(labelRepository, times(1)).createUserLabel(user, label);
    }

    @Test
    @DisplayName("선착순 라벨이 모두 마감되면 에외 발생")
    void createFirstComeFirstServedLabel_exception() {
        // given
        Long userId = 1L;
        User user = User.createUser(1L, "email", "password", "nickname", "imageUrl", Role.USER, Provider.LOCAL,
                LocalDateTime.now(), LocalDateTime.now(), false, 0);
        Label label = mock(Label.class);

        when(userService.findById(userId)).thenReturn(user);
        when(labelRepository.findByLabelType(LabelType.FIRST_COME)).thenReturn(List.of(label));
        when(labelRepository.existsUserLabelByUserAndLabel(user, label)).thenReturn(false);
        when(label.isOwnedCountValid()).thenReturn(false); // 수량이 부족한 상태

        // when & then
        assertThatThrownBy(() -> labelService.createFirstComeFirstServedLabel(userId))
                .isInstanceOf(FirstComeFirstServedLabelException.class)
                .hasMessage("모든 선착순 뽑기 라벨이 마감되었습니다.");

        verify(labelRepository, times(0)).updateOwnedCount(any(Label.class));
        verify(labelRepository, times(0)).createUserLabel(any(User.class), any(Label.class));
    }
}
