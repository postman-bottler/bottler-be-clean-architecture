package online.bottler.complaint.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;
import online.bottler.IdGenerator;
import online.bottler.complaint.application.port.ComplaintPersistencePort;
import online.bottler.complaint.application.port.KeywordComplaintPersistencePort;
import online.bottler.complaint.application.port.KeywordReplyComplaintPersistencePort;
import online.bottler.complaint.application.port.MapComplaintPersistencePort;
import online.bottler.complaint.application.port.MapReplyComplaintPersistencePort;
import online.bottler.complaint.domain.Complaint;
import online.bottler.complaint.domain.ComplaintType;
import online.bottler.global.exception.ApplicationException;
import online.bottler.notification.domain.NotificationType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

@SpringBootTest
class ComplaintServiceTest {

    @Autowired
    private ComplaintService complaintService;
    @SpyBean
    private KeywordComplaintPersistencePort keywordComplaintPersistencePort;
    @SpyBean
    private MapComplaintPersistencePort mapComplaintPersistencePort;
    @SpyBean
    private KeywordReplyComplaintPersistencePort keywordReplyComplaintPersistencePort;
    @SpyBean
    private MapReplyComplaintPersistencePort mapReplyComplaintPersistencePort;
    @Autowired
    private IdGenerator idGenerator;

    @DisplayName("편지를 신고한다.")
    @Test
    void complain() {
        // given
        Long letterId = idGenerator.generateId();
        Long reporterId = idGenerator.generateId();
        ComplaintCommand complaintCommand = new ComplaintCommand(ComplaintType.MAP_LETTER, letterId, reporterId,
                "욕설 사용");

        // when
        ComplaintResponse complaintResponse = complaintService.complain(complaintCommand);

        // then
        Optional<Complaint> find = mapComplaintPersistencePort.findByLetterIdAndReporterId(letterId, reporterId);
        assertThat(find).isPresent();
        assertThat(complaintResponse.id()).isEqualTo(find.get().getId());
    }

    @DisplayName("사용자가 이미 해당 편지를 신고한 경우, 예외가 발생한다.")
    @Test
    void duplicateComplaint() {
        // given®
        Long letterId = idGenerator.generateId();
        long reporterId = idGenerator.generateId();
        mapComplaintPersistencePort.save(Complaint.create(letterId, reporterId, "욕설 사용"));

        // when then
        assertThatThrownBy(() -> complaintService.complain(
                new ComplaintCommand(ComplaintType.MAP_LETTER, letterId, reporterId, "욕설 사용")))
                .isInstanceOf(ApplicationException.class);
    }

    @DisplayName("편지 신고가 3회 쌓이면, 경고가 필요하다.")
    @Test
    void needWarning() {
        // given
        Long letterId = idGenerator.generateId();
        mapComplaintPersistencePort.save(Complaint.create(letterId, idGenerator.generateId(), "욕설 사용"));
        mapComplaintPersistencePort.save(Complaint.create(letterId, idGenerator.generateId(), "욕설 사용"));
        mapComplaintPersistencePort.save(Complaint.create(letterId, idGenerator.generateId(), "욕설 사용"));

        // when
        boolean needWarning = complaintService.needWarning(ComplaintType.MAP_LETTER, letterId);

        // then
        assertThat(needWarning).isTrue();
    }

    @ParameterizedTest(name = "{0}")
    @CsvSource({"MAP_LETTER", "MAP_REPLY_LETTER", "KEYWORD_LETTER", "KEYWORD_REPLY_LETTER"})
    @DisplayName("편지 종류에 따라 적절한 포트를 사용한다.")
    public void isLetterNotification(ComplaintType complaintType) {
        // given
        Long letterId = idGenerator.generateId();
        Long reporterId = idGenerator.generateId();
        ComplaintCommand complaintCommand = new ComplaintCommand(complaintType, letterId, reporterId, "욕설 사용");

        // when
        complaintService.complain(complaintCommand);

        // then
        switch (complaintType) {
            case MAP_LETTER -> Mockito.verify(mapComplaintPersistencePort).save(Mockito.any(Complaint.class));
            case MAP_REPLY_LETTER -> Mockito.verify(mapReplyComplaintPersistencePort).save(Mockito.any(Complaint.class));
            case KEYWORD_LETTER -> Mockito.verify(keywordComplaintPersistencePort).save(Mockito.any(Complaint.class));
            case KEYWORD_REPLY_LETTER -> Mockito.verify(keywordReplyComplaintPersistencePort).save(Mockito.any(Complaint.class));
            default -> throw new IllegalArgumentException("Unknown complaint type: " + complaintType);
        }
    }
}