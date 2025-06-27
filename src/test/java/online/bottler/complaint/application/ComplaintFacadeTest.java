package online.bottler.complaint.application;

import static online.bottler.complaint.domain.ComplaintType.KEYWORD_LETTER;
import static online.bottler.complaint.domain.ComplaintType.KEYWORD_REPLY_LETTER;
import static online.bottler.complaint.domain.ComplaintType.MAP_LETTER;
import static online.bottler.complaint.domain.ComplaintType.MAP_REPLY_LETTER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.BDDMockito.given;

import java.util.Collection;
import java.util.List;
import online.bottler.IdGenerator;
import online.bottler.complaint.application.port.KeywordComplaintPersistencePort;
import online.bottler.complaint.application.port.KeywordReplyComplaintPersistencePort;
import online.bottler.complaint.application.port.MapComplaintPersistencePort;
import online.bottler.complaint.application.port.MapReplyComplaintPersistencePort;
import online.bottler.complaint.domain.Complaint;
import online.bottler.global.exception.ApplicationException;
import online.bottler.letter.application.LetterWithKeywordsService;
import online.bottler.letter.application.ReplyLetterService;
import online.bottler.mapletter.application.BlockMapLetterType;
import online.bottler.mapletter.application.MapLetterService;
import online.bottler.notification.application.NotificationService;
import online.bottler.user.application.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@DisplayName("신고 서비스 테스트")
public class ComplaintFacadeTest {
    @Autowired
    private ComplaintFacade complaintFacade;
    @Autowired
    private KeywordComplaintPersistencePort keywordComplaintPersistencePort;
    @Autowired
    private KeywordReplyComplaintPersistencePort keywordReplyComplaintPersistencePort;
    @Autowired
    private MapComplaintPersistencePort mapComplaintPersistencePort;
    @Autowired
    private MapReplyComplaintPersistencePort mapReplyComplaintPersistencePort;
    @Autowired
    private IdGenerator idGenerator;
    @MockBean
    private NotificationService notificationUseCase;
    @MockBean
    private LetterWithKeywordsService blockLetterUseCase;
    @MockBean
    private ReplyLetterService blockReplyLetterUseCase;
    @MockBean
    private MapLetterService mapLetterUseCase;
    @MockBean
    private UserService userUseCase;

    @DisplayName("키워드 편지 시나리오")
    @TestFactory
    Collection<DynamicTest> complainKeywordLetter() {
        // given
        long letterId = idGenerator.generateId();
        long reporterId = idGenerator.generateId();
        ComplaintCommand complaintCommand = new ComplaintCommand(KEYWORD_LETTER, letterId, reporterId, "욕설 사용");

        return List.of(
                dynamicTest("키워드 편지를 신고한다.", () -> {
                    // when
                    ComplaintResponse response = complaintFacade.complain(complaintCommand);

                    // then
                    assertThat(response.id()).isNotNull();
                    assertThat(response.description()).isEqualTo("욕설 사용");
                }),
                dynamicTest("한 유저가 같은 키워드 편지를 2회 이상 신고 시도할 경우, 예외가 발생한다.", () -> {
                    // when then
                    assertThatThrownBy(() -> complaintFacade.complain(complaintCommand))
                            .isInstanceOf(ApplicationException.class);
                })
        );
    }

    @DisplayName("키워드 답장 편지 시나리오")
    @TestFactory
    Collection<DynamicTest> complainKeywordReplyLetter() {
        // given
        long letterId = idGenerator.generateId();
        long reporterId = idGenerator.generateId();
        ComplaintCommand complaintCommand = new ComplaintCommand(KEYWORD_REPLY_LETTER, letterId, reporterId, "욕설 사용");
        return List.of(
                dynamicTest("키워드 답장 편지를 신고한다.", () -> {
                    // when
                    ComplaintResponse response = complaintFacade.complain(complaintCommand);

                    // then
                    assertThat(response.id()).isNotNull();
                    assertThat(response.description()).isEqualTo("욕설 사용");
                }),
                dynamicTest("한 유저가 같은 키워드 답장 편지를 2회 이상 신고 시도할 경우, 예외가 발생한다.", () -> {
                    // when then
                    assertThatThrownBy(() -> complaintFacade.complain(complaintCommand))
                            .isInstanceOf(ApplicationException.class);
                })
        );
    }

    @DisplayName("지도 편지 시나리오")
    @TestFactory
    Collection<DynamicTest> complainMapLetter() {
        // given
        long letterId = idGenerator.generateId();
        long reporterId = idGenerator.generateId();
        ComplaintCommand complaintCommand = new ComplaintCommand(MAP_LETTER, letterId, reporterId, "욕설 사용");

        return List.of(
                dynamicTest("지도 편지를 신고한다.", () -> {
                    // when
                    ComplaintResponse response = complaintFacade.complain(complaintCommand);

                    // then
                    assertThat(response.id()).isNotNull();
                    assertThat(response.description()).isEqualTo("욕설 사용");
                }),
                dynamicTest("한 유저가 같은 지도 편지를 2회 이상 신고 시도할 경우, 예외가 발생한다.", () -> {
                    // when then
                    assertThatThrownBy(() -> complaintFacade.complain(complaintCommand))
                            .isInstanceOf(ApplicationException.class);
                })
        );
    }

    @DisplayName("지도 답장 편지 시나리오")
    @TestFactory
    Collection<DynamicTest> complainMapReplyLetter() {
        // given
        long letterId = idGenerator.generateId();
        long reporterId = idGenerator.generateId();
        ComplaintCommand complaintCommand = new ComplaintCommand(MAP_REPLY_LETTER, letterId, reporterId, "욕설 사용");

        return List.of(
                dynamicTest("지도 편지를 신고한다.", () -> {
                    // when
                    ComplaintResponse response = complaintFacade.complain(complaintCommand);

                    // then
                    assertThat(response.id()).isNotNull();
                    assertThat(response.description()).isEqualTo("욕설 사용");
                }),
                dynamicTest("한 유저가 같은 지도 답장 편지를 2회 이상 신고 시도할 경우, 예외가 발생한다.", () -> {
                    // when then
                    assertThatThrownBy(() -> complaintFacade.complain(complaintCommand))
                            .isInstanceOf(ApplicationException.class);
                })
        );
    }

    @DisplayName("경고 알림이 필요한 경우, 작성자의 경고 횟수를 증가시킨다.")
    @Test
    void needWarningWithUserService() {
        // given
        Long letterId = idGenerator.generateId();
        Long reporterA = idGenerator.generateId();
        Long reporterB = idGenerator.generateId();
        keywordComplaintPersistencePort.save(Complaint.create(letterId, reporterA, "욕설 사용"));
        keywordComplaintPersistencePort.save(Complaint.create(letterId, reporterB, "욕설 사용"));
        Long writerId = idGenerator.generateId();

        given(blockLetterUseCase.softBlock(letterId))
                .willReturn(writerId);

        // when
        Long reporterC = idGenerator.generateId();
        complaintFacade.complain(new ComplaintCommand(KEYWORD_LETTER, letterId, reporterC, "욕설 사용"));

        // then
        Mockito.verify(userUseCase, Mockito.times(1))
                .updateWarningCount(writerId);
    }

    @DisplayName("경고 알림이 필요한 경우, 작성자에게 알림을 전송한다.")
    @Test
    void needWarningWithNotificationService() {
        // given
        Long letterId = idGenerator.generateId();
        Long reporterA = idGenerator.generateId();
        Long reporterB = idGenerator.generateId();
        keywordComplaintPersistencePort.save(Complaint.create(letterId, reporterA, "욕설 사용"));
        keywordComplaintPersistencePort.save(Complaint.create(letterId, reporterB, "욕설 사용"));
        Long writerId = idGenerator.generateId();

        given(blockLetterUseCase.softBlock(letterId))
                .willReturn(writerId);

        // when
        Long reporterC = idGenerator.generateId();
        complaintFacade.complain(new ComplaintCommand(KEYWORD_LETTER, letterId, reporterC, "욕설 사용"));

        // then
        Mockito.verify(notificationUseCase, Mockito.times(1))
                .sendWarningNotification(writerId);
    }

    @DisplayName("지도 편지 경고가 필요할 경우, 해당 지도 편지는 블락된다.")
    @Test
    void blockMapLetter() {
        // given
        Long letterId = idGenerator.generateId();
        mapComplaintPersistencePort.save(Complaint.create(letterId, idGenerator.generateId(), "욕설 사용"));
        mapComplaintPersistencePort.save(Complaint.create(letterId, idGenerator.generateId(), "욕설 사용"));

        // when
        complaintFacade.complain(new ComplaintCommand(MAP_LETTER, letterId, idGenerator.generateId(), "욕설 사용"));

        // then
        Mockito.verify(mapLetterUseCase, Mockito.times(1))
                .letterBlock(BlockMapLetterType.MAP_LETTER, letterId);
    }

    @DisplayName("지도 답장 편지 경고가 필요할 경우, 해당 지도 답장 편지는 블락된다.")
    @Test
    void blockMapReplyLetter() {
        // given
        Long letterId = idGenerator.generateId();
        mapReplyComplaintPersistencePort.save(Complaint.create(letterId, idGenerator.generateId(), "욕설 사용"));
        mapReplyComplaintPersistencePort.save(Complaint.create(letterId, idGenerator.generateId(), "욕설 사용"));

        // when
        complaintFacade.complain(new ComplaintCommand(MAP_REPLY_LETTER, letterId, idGenerator.generateId(), "욕설 사용"));

        // then
        Mockito.verify(mapLetterUseCase, Mockito.times(1))
                .letterBlock(BlockMapLetterType.REPLY, letterId);
    }

    @DisplayName("키워드 편지 경고가 필요할 경우, 해당 키워드 편지는 블락된다.")
    @Test
    void blockKeywordLetter() {
        // given
        Long letterId = idGenerator.generateId();
        keywordComplaintPersistencePort.save(Complaint.create(letterId, idGenerator.generateId(), "욕설 사용"));
        keywordComplaintPersistencePort.save(Complaint.create(letterId, idGenerator.generateId(), "욕설 사용"));

        // when
        complaintFacade.complain(new ComplaintCommand(KEYWORD_LETTER, letterId, idGenerator.generateId(), "욕설 사용"));

        // then
        Mockito.verify(blockLetterUseCase, Mockito.times(1))
                .softBlock(letterId);
    }

    @DisplayName("지도 답장 편지 경고가 필요할 경우, 해당 지도 답장 편지는 블락된다.")
    @Test
    void blockKeywordReplyLetter() {
        // given
        Long letterId = idGenerator.generateId();
        keywordReplyComplaintPersistencePort.save(Complaint.create(letterId, idGenerator.generateId(), "욕설 사용"));
        keywordReplyComplaintPersistencePort.save(Complaint.create(letterId, idGenerator.generateId(), "욕설 사용"));

        // when
        complaintFacade.complain(
                new ComplaintCommand(KEYWORD_REPLY_LETTER, letterId, idGenerator.generateId(), "욕설 사용"));

        // then
        Mockito.verify(blockReplyLetterUseCase, Mockito.times(1))
                .softBlock(letterId);
    }
}
