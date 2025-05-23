package online.bottler.letter.adapter.out.persistence.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import online.bottler.letter.adapter.out.persistence.entity.LetterKeywordEntity;

public interface LetterKeywordJpaRepository extends JpaRepository<LetterKeywordEntity, Long> {
    @Modifying
    @Query(value = "UPDATE letter_keyword SET is_deleted = false WHERE letter_id IN :letterIds", nativeQuery = true)
    void softDeleteByIds(@Param("letterIds") List<Long> letterIds);

    @Modifying
    @Query(value = "UPDATE letter_keyword SET is_deleted = false WHERE letter_id = :letterId", nativeQuery = true)
    void softDeleteById(@Param("letterIds") Long letterId);
}
