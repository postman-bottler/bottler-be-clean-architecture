package online.bottler.mapletter.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import online.bottler.RedisTestContainersConfig;
import online.bottler.global.exception.AdaptorException;
import online.bottler.global.exception.ApplicationException;
import online.bottler.label.application.LabelService;
import online.bottler.mapletter.adaptor.out.persistence.entity.MapLetterEntity;
import online.bottler.mapletter.adaptor.out.persistence.repository.MapLetterJpaRepository;
import online.bottler.mapletter.application.command.CreateReplyMapLetterCommand;
import online.bottler.mapletter.application.command.CreateTargetMapLetterCommand;
import online.bottler.mapletter.application.port.in.MapLetterReplyUseCase;
import online.bottler.mapletter.application.port.in.MapLetterUseCase;
import online.bottler.mapletter.application.port.out.MapLetterPersistencePort;
import online.bottler.mapletter.application.port.out.RecentReplyCachePort;
import online.bottler.mapletter.application.port.out.ReplyMapLetterPersistencePort;
import online.bottler.mapletter.domain.MapLetter;
import online.bottler.mapletter.domain.MapLetterType;
import online.bottler.mapletter.domain.ReplyMapLetter;
import online.bottler.notification.application.port.NotificationUseCase;
import online.bottler.user.adapter.out.persistence.entity.UserEntity;
import online.bottler.user.adapter.out.persistence.repository.UserJpaRepository;
import online.bottler.user.application.UserService;
import online.bottler.user.application.port.in.UserUseCase;
import online.bottler.user.domain.Provider;
import online.bottler.user.domain.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@Import(RedisTestContainersConfig.class)
@Transactional
class MapLetterCreateFacadeTest {

    @Autowired
    private MapLetterCreateFacade mapLetterCreateFacade;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private MapLetterJpaRepository mapLetterJpaRepository;

    @DisplayName("타겟 편지 생성에 성공한다.")
    @Test
    void createTargetMapLetterTest() {
        //given
        Long userId = 1L;
        CreateTargetMapLetterCommand command = new CreateTargetMapLetterCommand(
                "title", "content", "description", new BigDecimal("37.5665"),
                new BigDecimal("126.9780"), "font", "paper", "label", "targetUserName");

        userJpaRepository.save(createUserEntity());

        //when
        MapLetter targetMapLetter = mapLetterCreateFacade.createTargetMapLetter(command, userId);

        //then
        assertThat(targetMapLetter).isNotNull();
        assertThat(targetMapLetter.getTitle()).isEqualTo("title");
        assertThat(targetMapLetter.getType()).isEqualTo(MapLetterType.PRIVATE);
        assertThat(targetMapLetter.getCreateUserId()).isEqualTo(userId);
    }

    @DisplayName("타겟 편지 생성시 targetUserNickName에 대한 Target User가 존재하지 않으면 예외가 발생한다.")
    @Test
    void targetUserNotFoundByNicknameTest() {
        //given
        Long userId = 1L;
        CreateTargetMapLetterCommand command = new CreateTargetMapLetterCommand(
                "title", "content", "description", new BigDecimal("37.5665"),
                new BigDecimal("126.9780"), "font", "paper", "label", "targetUserName");

        //when, then
        assertThatThrownBy(() -> mapLetterCreateFacade.createTargetMapLetter(command, userId))
                .isInstanceOf(AdaptorException.class)
                .hasMessage("해당 닉네임에 대한 유저를 찾을 수 없습니다.");
    }

    @DisplayName("답장 편지를 생성한다.")
    @Test
    void createReplyMapLetterTest() {
        //given
        Long userId = 1L;
        MapLetterEntity sourceLetter = mapLetterJpaRepository.save(createMapLetterEntity());
        CreateReplyMapLetterCommand command = new CreateReplyMapLetterCommand(
                sourceLetter.getMapLetterId(), "content", " font", "paper", "label");

        //when
        ReplyMapLetter replyMapLetter = mapLetterCreateFacade.createReplyMapLetter(command, userId);

        //then
        assertThat(replyMapLetter).isNotNull();
        assertThat(replyMapLetter.getSourceLetterId()).isEqualTo(sourceLetter.getMapLetterId());
        assertThat(replyMapLetter.getContent()).isEqualTo(command.content());
    }

    @DisplayName("답장 편지 생성시 원본 편지가 없으면 예외가 발생한다.")
    @Test
    void originalMapLetterDoesNotExistTest() {
        //given
        CreateReplyMapLetterCommand command = new CreateReplyMapLetterCommand(
                1L, "content", " font", "paper", "label");

        //when, then
        assertThatThrownBy(() -> mapLetterCreateFacade.createReplyMapLetter(command, 1L))
                .isInstanceOf(AdaptorException.class)
                .hasMessage("원본 편지를 찾을 수 없습니다. 편지가 존재하지 않거나 삭제되었습니다.");
    }

    @DisplayName("답장 편지 생성시 해당 편지에 이미 답장을 했으면 예외가 발생한다.")
    @Test
    void alreadyRepliedToMapLetterTest() {
        //given
        Long userId = 1L;
        MapLetterEntity sourceLetter = mapLetterJpaRepository.save(createMapLetterEntity());
        CreateReplyMapLetterCommand command = new CreateReplyMapLetterCommand(
                sourceLetter.getMapLetterId(), "content", " font", "paper", "label");

        mapLetterCreateFacade.createReplyMapLetter(command, userId);

        //when, then
        assertThatThrownBy(() -> mapLetterCreateFacade.createReplyMapLetter(command, userId))
                .isInstanceOf(ApplicationException.class)
                .hasMessage("해당 지도 편지에 이미 답장을 했습니다.");
    }

    private UserEntity createUserEntity() {
        return UserEntity.builder()
                .email("target@example.com")
                .nickname("targetUserName")
                .password("pw")
                .imageUrl("http://example.com/img.png")
                .role(Role.USER)
                .provider(Provider.LOCAL)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isDeleted(false)
                .warningCount(0)
                .build();
    }

    private MapLetterEntity createMapLetterEntity() {
        return MapLetterEntity.builder()
                .title("title")
                .content("content")
                .latitude(new BigDecimal("37.5665"))
                .longitude(new BigDecimal("126.9780"))
                .font("font")
                .paper("paper")
                .label("label")
                .description("description")
                .type(MapLetterType.PUBLIC)
                .createUserId(2L)
                .build();
    }
}
