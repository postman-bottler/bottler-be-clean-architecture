package online.bottler.letter.adapter.out.persistence.repository;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import online.bottler.letter.domain.UserKeyword;

@Repository
@RequiredArgsConstructor
public class UserKeywordJdbcRepository {

    private static final String INSERT_SQL = "INSERT INTO user_keyword (user_id, keyword) VALUES (?, ?)";
    private static final String DELETE_SQL = "DELETE FROM user_keyword WHERE user_id = ?";

    private final JdbcTemplate jdbcTemplate;

    public void batchInsertKeywords(List<UserKeyword> keywords) {
        jdbcTemplate.batchUpdate(INSERT_SQL, toBatchKeywordParams(keywords));
    }

    public void deleteAllByUserId(Long userId) {
        jdbcTemplate.update(DELETE_SQL, userId);
    }

    private List<Object[]> toBatchKeywordParams(List<UserKeyword> keywords) {
        return keywords.stream().map(entity -> new Object[]{entity.getUserId(), entity.getKeyword()}).toList();
    }
}
