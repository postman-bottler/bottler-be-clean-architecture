package online.bottler.complaint.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import online.bottler.complaint.domain.Complaint;
import online.bottler.complaint.domain.Complaints;

@DisplayName("키워드 답장 편지 리포지토리 테스트")
@SpringBootTest
@Transactional
class KeywordReplyComplaintPersistenceAdapterTest {

    @Autowired
    private KeywordReplyComplaintPersistenceAdapter keywordReplyComplaintPersistenceAdapter;

    @DisplayName("새로운 신고를 저장한다.")
    @Test
    void save() {
        // given
        Complaint complaint = Complaint.create(1L, 1L, "욕설 사용");

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
        keywordReplyComplaintPersistenceAdapter.save(Complaint.create(1L, 1L, "설명"));

        // when
        Complaints find = keywordReplyComplaintPersistenceAdapter.findByLetterId(1L);

        // then
        assertThat(find.getComplaints()).hasSize(1)
                .extracting("letterId", "reporterId", "description")
                .contains(tuple(1L, 1L, "설명"));
    }

    @Test
    @DisplayName("편지 ID로 조회한 신고 리스트는 MutableList이어야 한다.")
    public void findByLetterIdWithMutable() {
        // given
        keywordReplyComplaintPersistenceAdapter.save(Complaint.create(1L, 1L, "설명"));

        // when
        Complaints find = keywordReplyComplaintPersistenceAdapter.findByLetterId(1L);

        // then
        List<Complaint> complaints = find.getComplaints();
        assertDoesNotThrow((() -> complaints.add(Complaint.create(2L, 1L, "설명"))));
    }
}
