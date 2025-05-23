package online.bottler.keyword.infra;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import online.bottler.TestBase;
import online.bottler.letter.domain.UserKeyword;
import online.bottler.letter.adapter.out.persistence.entity.UserKeywordEntity;
import online.bottler.letter.adapter.out.persistence.repository.UserKeywordJpaRepository;

@DataJpaTest
@ActiveProfiles("test")
class UserKeywordJpaRepositoryTest extends TestBase {

    @Autowired
    private UserKeywordJpaRepository userKeywordJpaRepository;

    private static final Long USER_ID_1 = 1L;
    private static final Long USER_ID_2 = 2L;

    @BeforeEach
    void setUp() {
        userKeywordJpaRepository.saveAll(List.of(
                UserKeywordEntity.from(UserKeyword.builder().userId(USER_ID_1).keyword("keyword1").build()),
                UserKeywordEntity.from(UserKeyword.builder().userId(USER_ID_1).keyword("keyword2").build()),
                UserKeywordEntity.from(UserKeyword.builder().userId(USER_ID_2).keyword("keyword3").build())
        ));
    }

    @Test
    @DisplayName("특정 userId로 모든 키워드 조회")
    void findAllByUserId() {
        // when
        List<UserKeywordEntity> keywords = userKeywordJpaRepository.findAllByUserId(USER_ID_1);

        // then
        assertThat(keywords).hasSize(2);
        assertThat(keywords).extracting(entity -> entity.toDomain().getKeyword())
                .containsExactlyInAnyOrder("keyword1", "keyword2");
    }

    @Test
    @DisplayName("특정 userId로 키워드 문자열 조회")
    void findKeywordsByUserId() {
        // when
        List<String> keywords = userKeywordJpaRepository.findKeywordsByUserId(USER_ID_1);

        // then
        assertThat(keywords).hasSize(2);
        assertThat(keywords).containsExactlyInAnyOrder("keyword1", "keyword2");
    }
}
