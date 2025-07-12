package online.bottler.mapletter.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import online.bottler.RedisTestContainersConfig;
import online.bottler.global.exception.ApplicationException;
import online.bottler.global.exception.CommonForbiddenException;
import online.bottler.global.exception.DomainException;
import online.bottler.mapletter.adaptor.out.persistence.entity.MapLetterEntity;
import online.bottler.mapletter.adaptor.out.persistence.repository.MapLetterJpaRepository;
import online.bottler.mapletter.application.command.DeleteArchivedLettersCommand;
import online.bottler.mapletter.application.port.out.MapLetterArchivePersistencePort;
import online.bottler.mapletter.application.port.out.MapLetterPersistencePort;
import online.bottler.mapletter.application.response.FindAllArchiveLettersResponse;
import online.bottler.mapletter.domain.MapLetter;
import online.bottler.mapletter.domain.MapLetterArchive;
import online.bottler.mapletter.domain.MapLetterType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@Import(RedisTestContainersConfig.class)
@Transactional
class MapLetterArchiveServiceTest {

    @MockBean
    private MapLetterPersistencePort mapLetterPersistencePort;

    @Autowired
    private MapLetterArchivePersistencePort mapLetterArchivePersistencePort;

    @Autowired
    private MapLetterArchiveService mapLetterArchiveService;

    @Autowired
    private MapLetterJpaRepository mapLetterJpaRepository;

    @AfterEach
    void tearDown() {
        mapLetterJpaRepository.deleteAllInBatch();
    }

    @DisplayName("letter Id와 user Id를 받아 편지를 보관한다.")
    @Test
    void letterArchiveTest() {
        //given
        Long userId = 1L;
        Long letterId = 2L;

        MapLetter dummyLetter = MapLetter.builder().id(letterId).build();
        given(mapLetterPersistencePort.findById(letterId))
                .willReturn(dummyLetter);

        //when
        MapLetterArchive mapLetterArchive = mapLetterArchiveService.mapLetterArchive(letterId, userId);

        //then
        assertThat(mapLetterArchive).isNotNull();
        assertThat(userId).isEqualTo(mapLetterArchive.getUserId());
        assertThat(letterId).isEqualTo(mapLetterArchive.getMapLetterId());
    }

    @DisplayName("이미 보관되어 있는 편지를 보관 시도 할 경우, 예외가 발생한다.")
    @Test
    void letterAlreadyArchivedTest() {
        //given
        Long userId = 1L;
        Long letterId = 2L;

        MapLetter dummyLetter = MapLetter.builder().id(letterId).build();
        given(mapLetterPersistencePort.findById(letterId))
                .willReturn(dummyLetter);

        mapLetterArchiveService.mapLetterArchive(letterId, userId);

        //when, then
        assertThatThrownBy(() -> mapLetterArchiveService.mapLetterArchive(letterId, userId))
                .isInstanceOf(ApplicationException.class)
                .hasMessage("해당 지도 편지가 이미 저장되어 있습니다.");
    }

    @DisplayName("편지의 타입이 Private인 편지를 보관 시도 할 경우, 예외가 발생한다.")
    @Test
    void cannotArchivePrivateLetter() {
        //given
        Long userId = 1L;
        Long letterId = 2L;

        MapLetter dummyLetter = MapLetter.builder().id(letterId).type(MapLetterType.PRIVATE).build();
        given(mapLetterPersistencePort.findById(letterId))
                .willReturn(dummyLetter);

        //when, then
        assertThatThrownBy(() -> mapLetterArchiveService.mapLetterArchive(letterId, userId))
                .isInstanceOf(CommonForbiddenException.class)
                .hasMessage("편지를 저장할 수 있는 권한이 없습니다.");
    }

    @DisplayName("삭제된 편지를 보관 시도 할 경우, 예외가 발생한다.")
    @Test
    void cannotArchiveDeletedLetter() {
        //given
        Long userId = 1L;
        Long letterId = 2L;

        MapLetter dummyLetter = MapLetter.builder().id(letterId).isDeleted(true).build();
        given(mapLetterPersistencePort.findById(letterId))
                .willReturn(dummyLetter);

        //when, then
        assertThatThrownBy(() -> mapLetterArchiveService.mapLetterArchive(letterId, userId))
                .isInstanceOf(DomainException.class)
                .hasMessage("해당 편지는 삭제되었습니다.");
    }

    @DisplayName("블락된 편지를 보관 시도 할 경우, 예외가 발생한다.")
    @Test
    void cannotArchiveBlockedLetter() {
        //given
        Long userId = 1L;
        Long letterId = 2L;

        MapLetter dummyLetter = MapLetter.builder().id(letterId).isBlocked(true).build();
        given(mapLetterPersistencePort.findById(letterId))
                .willReturn(dummyLetter);

        //when, then
        assertThatThrownBy(() -> mapLetterArchiveService.mapLetterArchive(letterId, userId))
                .isInstanceOf(DomainException.class)
                .hasMessage("해당 편지는 신고당한 편지입니다.");
    }

    @DisplayName("보관한 편지들을 조회한다.")
    @Test
    void findArchivedLettersTest() {
        //given
        Long userId = 1L;

        MapLetterEntity m1 = mapLetterJpaRepository.save(createMapLetterEntity());
        MapLetterEntity m2 = mapLetterJpaRepository.save(createMapLetterEntity());
        MapLetterArchive mapLetterArchive1 = createMapLetterArchive(m1.getMapLetterId(), userId);
        MapLetterArchive mapLetterArchive2 = createMapLetterArchive(m2.getMapLetterId(), userId);
        mapLetterArchivePersistencePort.save(mapLetterArchive1);
        mapLetterArchivePersistencePort.save(mapLetterArchive2);

        //when
        Page<FindAllArchiveLettersResponse> archiveLettersPage
                = mapLetterArchiveService.findArchiveLetters(1, 9, userId);

        //then
        List<FindAllArchiveLettersResponse> archiveLetters = archiveLettersPage.getContent();

        assertThat(archiveLetters).extracting("letterId")
                .containsExactlyInAnyOrder(m1.getMapLetterId(), m2.getMapLetterId());
    }

    @DisplayName("보관된 편지 조회 시, 페이지 번호가 0 이하이면 예외가 발생한다.")
    @Test
    void shouldThrowExceptionWhenPageIsLessThanOne() {
        //given
        int page = 0;

        //when, then
        assertThatThrownBy(() -> mapLetterArchiveService.findArchiveLetters(page, 9, 1L))
                .isInstanceOf(ApplicationException.class)
                .hasMessage("페이지가 존재하지 않습니다.");

    }

    @DisplayName("보관된 편지 조회 시, 요청한 페이지가 전체 페이지 수보다 클 경우 예외가 발생한다")
    @Test
    void shouldThrowExceptionWhenPageExceedsTotalPages() {
        //given
        Long userId = 1L;

        MapLetterEntity m1 = mapLetterJpaRepository.save(createMapLetterEntity());
        MapLetterArchive mapLetterArchive1 = createMapLetterArchive(m1.getMapLetterId(), userId);
        mapLetterArchivePersistencePort.save(mapLetterArchive1);

        //when, then
        assertThatThrownBy(() -> mapLetterArchiveService.findArchiveLetters(2, 9, 1L))
                .isInstanceOf(ApplicationException.class)
                .hasMessage("페이지가 존재하지 않습니다.");

        assertThatThrownBy(() -> mapLetterArchiveService.findArchiveLetters(5, 9, 1L))
                .isInstanceOf(ApplicationException.class)
                .hasMessage("페이지가 존재하지 않습니다.");
    }

    @DisplayName("보관된 편지 조회 시, 편지가 없는 경우 빈 페이지를 반환한다.")
    @Test
    void shouldReturnEmptyPageWhenNoArchivedLettersExist() {
        //when
        Page<FindAllArchiveLettersResponse> archiveLetters = mapLetterArchiveService.findArchiveLetters(1, 9, 1L);

        //then
        assertThat(archiveLetters).isNotNull();
        assertThat(archiveLetters.getContent()).isEmpty();
        assertThat(archiveLetters.getTotalElements()).isEqualTo(0);
        assertThat(archiveLetters.getTotalPages()).isEqualTo(0);
    }

    @DisplayName("보관한 편지를 삭제한다.")
    @Test
    void deleteArchivedLettersTest() {
        //given
        Long userId = 1L;

        MapLetterEntity m1 = mapLetterJpaRepository.save(createMapLetterEntity());
        MapLetterEntity m2 = mapLetterJpaRepository.save(createMapLetterEntity());
        MapLetterArchive a1 = createMapLetterArchive(m1.getMapLetterId(), userId);
        MapLetterArchive a2 = createMapLetterArchive(m2.getMapLetterId(), userId);
        MapLetterArchive mapLetterArchive1 = mapLetterArchivePersistencePort.save(a1);
        MapLetterArchive mapLetterArchive2 = mapLetterArchivePersistencePort.save(a2);

        DeleteArchivedLettersCommand deleteArchivedLettersCommand = new DeleteArchivedLettersCommand(
                List.of(
                        mapLetterArchive1.getMapLetterArchiveId(),
                        mapLetterArchive2.getMapLetterArchiveId()
                )
        );

        //when
        mapLetterArchiveService.deleteArchivedLetter(deleteArchivedLettersCommand, userId);

        //then
        List<MapLetterArchive> remaining = mapLetterArchivePersistencePort.findAllById(
                deleteArchivedLettersCommand.letterIds());

        assertThat(remaining).isEmpty();
    }

    @DisplayName("보관한 편지를 삭제할 때, 일부 아이디가 존재하지 않으면 예외가 발생한다.")
    @Test
    void throwsExceptionIfSomeArchivedLetterIdsAreNotFound() {
        //given
        Long userId = 1L;

        MapLetterEntity m1 = mapLetterJpaRepository.save(createMapLetterEntity());
        MapLetterArchive mapLetterArchive1 = createMapLetterArchive(m1.getMapLetterId(), userId);
        mapLetterArchivePersistencePort.save(mapLetterArchive1);

        DeleteArchivedLettersCommand deleteArchivedLettersCommand = new DeleteArchivedLettersCommand(
                List.of(
                        mapLetterArchive1.getMapLetterId(),
                        1000L,
                        mapLetterArchive1.getMapLetterId()+1
                )
        );

        //when, then
        assertThatThrownBy(()-> mapLetterArchiveService.deleteArchivedLetter(deleteArchivedLettersCommand, userId))
                .isInstanceOf(ApplicationException.class)
                .hasMessage("삭제할 지도 편지를 찾을 수 없습니다.");
    }

    @DisplayName("편지가 보관되어있으면 true를 반환한다.")
    @Test
    void shouldReturnTrueWhenLetterIsArchived() {
        //given
        Long userId = 1L;

        MapLetterEntity m1 = mapLetterJpaRepository.save(createMapLetterEntity());
        MapLetterArchive a1 = createMapLetterArchive(m1.getMapLetterId(), userId);
        MapLetterArchive mapLetterArchive1 = mapLetterArchivePersistencePort.save(a1);

        //when
        boolean archived = mapLetterArchiveService.isArchived(m1.getMapLetterId(), userId);

        //then
        assertThat(archived).isTrue();
    }

    @DisplayName("편지가 보관되어있지 않으면 false를 반환한다.")
    @Test
    void shouldReturnFalseWhenLetterIsNotArchived() {
        //given
        Long userId = 1L;

        MapLetterEntity m1 = mapLetterJpaRepository.save(createMapLetterEntity());
        MapLetterEntity m2 = mapLetterJpaRepository.save(createMapLetterEntity());
        MapLetterArchive a1 = createMapLetterArchive(m1.getMapLetterId(), userId);
        MapLetterArchive mapLetterArchive1 = mapLetterArchivePersistencePort.save(a1);

        //when
        boolean archived = mapLetterArchiveService.isArchived(m2.getMapLetterId(), userId);

        //then
        assertThat(archived).isFalse();
    }

    private MapLetterArchive createMapLetterArchive(Long letterId, Long userId) {
        return MapLetterArchive.builder()
                .mapLetterId(letterId)
                .userId(userId)
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
                .targetUserId(null)
                .createUserId(10L)
                .createdAt(LocalDateTime.of(2025, 7, 11, 17, 20))
                .updatedAt(LocalDateTime.of(2025, 7, 11, 17, 20))
                .isDeleted(false)
                .isBlocked(false)
                .isRead(false)
                .isRecipientDeleted(false)
                .build();
    }
}
