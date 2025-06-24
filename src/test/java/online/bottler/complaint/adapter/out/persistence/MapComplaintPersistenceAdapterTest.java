package online.bottler.complaint.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import online.bottler.IdGenerator;
import online.bottler.complaint.domain.Complaint;
import online.bottler.complaint.domain.Complaints;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("지도 편지 리포지토리 테스트")
@SpringBootTest
public class MapComplaintPersistenceAdapterTest {
    @Autowired
    private MapComplaintPersistenceAdapter mapComplaintPersistenceAdapter;
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
        mapComplaintPersistenceAdapter.save(complaint);

        // then
        Complaints complaints = mapComplaintPersistenceAdapter.findByLetterId(complaint.getLetterId());
        assertThat(complaints.getComplaints()).hasSize(1);
    }

    @DisplayName("편지의 신고를 조회한다.")
    @Test
    void findByLetterId() {
        // given
        Long letterId = idGenerator.generateId();
        Long reporterId = idGenerator.generateId();
        mapComplaintPersistenceAdapter.save(Complaint.create(letterId, reporterId, "설명"));

        // when
        Complaints find = mapComplaintPersistenceAdapter.findByLetterId(letterId);

        // then
        assertThat(find.getComplaints()).hasSize(1)
                .extracting("letterId", "reporterId", "description")
                .contains(tuple(letterId, reporterId, "설명"));
    }

    @Test
    @DisplayName("편지 ID로 조회한 신고 리스트는 MutableList이어야 한다.")
    public void findByLetterIdWithMutable() {
        // GIVEN
        Long letterId = idGenerator.generateId();
        Long reporterId = idGenerator.generateId();
        mapComplaintPersistenceAdapter.save(Complaint.create(letterId, reporterId, "설명"));

        // WHEN
        Complaints find = mapComplaintPersistenceAdapter.findByLetterId(letterId);

        // THEN
        List<Complaint> complaints = find.getComplaints();
        assertDoesNotThrow(
                (() -> complaints.add(Complaint.create(idGenerator.generateId(), idGenerator.generateId(), "설명"))));
    }
}
