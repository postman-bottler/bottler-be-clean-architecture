package online.bottler.complaint.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;

import online.bottler.IdGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import online.bottler.complaint.domain.Complaint;
import online.bottler.complaint.domain.Complaints;

@DisplayName("키워드 답장 편지 리포지토리 테스트")
@SpringBootTest
class KeywordReplyComplaintPersistenceAdapterTest {

    @Autowired
    private KeywordReplyComplaintPersistenceAdapter keywordReplyComplaintPersistenceAdapter;
    @Autowired
    private IdGenerator idGenerator;

    @DisplayName("새로운 신고를 저장한다.")
    @Test
    void save() {
        // given
        Long letterId = idGenerator.generateId();
        Long reporterId = idGenerator.generateId();
        Complaint complaint = Complaint.create(letterId, reporterId, "욕설 사용");

        // when
        keywordReplyComplaintPersistenceAdapter.save(complaint);

        // then
        Complaints complaints = keywordReplyComplaintPersistenceAdapter.findByLetterId(complaint.getLetterId());
        assertThat(complaints.getComplaints()).hasSize(1);
    }

    @DisplayName("편지의 신고를 조회한다.")
    @Test
    void findByLetterId() {
        // given
        Long letterId = idGenerator.generateId();
        Long reporterId = idGenerator.generateId();
        keywordReplyComplaintPersistenceAdapter.save(Complaint.create(letterId, reporterId, "설명"));

        // when
        Complaints find = keywordReplyComplaintPersistenceAdapter.findByLetterId(letterId);

        // then
        assertThat(find.getComplaints()).hasSize(1)
                .extracting("letterId", "reporterId", "description")
                .contains(tuple(letterId, reporterId, "설명"));
    }

    @Test
    @DisplayName("편지 ID로 조회한 신고 리스트는 MutableList이어야 한다.")
    public void findByLetterIdWithMutable() {
        // given
        Long letterId = idGenerator.generateId();
        Long reporterId = idGenerator.generateId();
        keywordReplyComplaintPersistenceAdapter.save(Complaint.create(letterId, reporterId, "설명"));

        // when
        Complaints find = keywordReplyComplaintPersistenceAdapter.findByLetterId(letterId);

        // then
        List<Complaint> complaints = find.getComplaints();
        assertDoesNotThrow((() -> complaints.add(Complaint.create(letterId, idGenerator.generateId(), "설명"))));
    }
}
