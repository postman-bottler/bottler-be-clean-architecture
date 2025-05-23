package online.bottler.letter.adapter.out.persistence.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.adapter.out.persistence.entity.QLetterBoxEntity;
import online.bottler.letter.adapter.out.persistence.entity.QLetterEntity;
import online.bottler.letter.adapter.out.persistence.entity.QReplyLetterEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import online.bottler.letter.application.response.LetterSummaryResponse;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.LetterType;

@Repository
@RequiredArgsConstructor
public class LetterBoxQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<LetterSummaryResponse> fetchLetters(Long userId, BoxType boxType, Pageable pageable) {
        QLetterBoxEntity letterBox = QLetterBoxEntity.letterBoxEntity;
        QLetterEntity letter = QLetterEntity.letterEntity;
        QReplyLetterEntity replyLetter = QReplyLetterEntity.replyLetterEntity;

        StringExpression letterTitle = getLetterTitle(letterBox, letter, replyLetter);
        StringExpression letterLabel = getLetterLabel(letterBox, letter, replyLetter);

        BooleanBuilder condition = buildFetchCondition(userId, boxType);

        return queryFactory
                .select(Projections.constructor(
                        LetterSummaryResponse.class,
                        letterBox.letterId,
                        letterTitle,
                        letterLabel,
                        letterBox.letterType,
                        letterBox.boxType,
                        letterBox.createdAt
                ))
                .from(letterBox)
                .leftJoin(letter).on(letterBox.letterId.eq(letter.id)
                        .and(letterBox.letterType.eq(LetterType.LETTER)))
                .leftJoin(replyLetter).on(letterBox.letterId.eq(replyLetter.id)
                        .and(letterBox.letterType.eq(LetterType.REPLY_LETTER)))
                .where(condition)
                .orderBy(letterBox.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    public List<Long> findReceivedLetterIdsByUserId(Long userId) {
        QLetterBoxEntity letterBox = QLetterBoxEntity.letterBoxEntity;
        BooleanBuilder condition = buildFetchCondition(userId, BoxType.RECEIVE);

        return queryFactory
                .select(letterBox.letterId)
                .from(letterBox)
                .where(condition)
                .fetch();
    }

    public long countLetters(Long userId, BoxType boxType) {
        QLetterBoxEntity letterBox = QLetterBoxEntity.letterBoxEntity;
        BooleanBuilder condition = buildFetchCondition(userId, boxType);

        return queryFactory
                .select(letterBox.id)
                .from(letterBox)
                .where(condition)
                .fetch()
                .size();
    }

    public void deleteByCondition(List<Long> letterIds, LetterType letterType, BoxType boxType) {
        QLetterBoxEntity letterBox = QLetterBoxEntity.letterBoxEntity;
        BooleanBuilder condition = buildDeletionCondition(null, letterIds, letterType, boxType);

        queryFactory
                .delete(letterBox)
                .where(condition)
                .execute();
    }



    public void deleteByConditionAndUserId(List<Long> letterIds, LetterType letterType, BoxType boxType, Long userId) {
        QLetterBoxEntity letterBox = QLetterBoxEntity.letterBoxEntity;

        BooleanBuilder condition = buildDeletionCondition(userId, letterIds, letterType, boxType);

        queryFactory
                .delete(letterBox)
                .where(condition)
                .execute();
    }

    public void deleteAllByUserIdAndBoxType(Long userId, BoxType boxType) {
        QLetterBoxEntity letterBox = QLetterBoxEntity.letterBoxEntity;
        BooleanBuilder condition = buildDeletionCondition(userId, Collections.emptyList(), LetterType.NONE, boxType);

        queryFactory.delete(letterBox)
                .where(condition)
                .execute();
    }

    private StringExpression getLetterTitle(QLetterBoxEntity letterBox, QLetterEntity letter,
                                            QReplyLetterEntity replyLetter) {
        return new CaseBuilder()
                .when(letterBox.letterType.eq(LetterType.LETTER)).then(letter.title)
                .when(letterBox.letterType.eq(LetterType.REPLY_LETTER)).then(replyLetter.title)
                .otherwise("Unknown Title");
    }

    private StringExpression getLetterLabel(QLetterBoxEntity letterBox, QLetterEntity letter,
                                            QReplyLetterEntity replyLetter) {
        return new CaseBuilder()
                .when(letterBox.letterType.eq(LetterType.LETTER)).then(letter.label)
                .when(letterBox.letterType.eq(LetterType.REPLY_LETTER)).then(replyLetter.label)
                .otherwise("Unknown Label");
    }

    private BooleanBuilder buildFetchCondition(Long userId, BoxType boxType) {
        QLetterBoxEntity letterBox = QLetterBoxEntity.letterBoxEntity;
        BooleanBuilder condition = new BooleanBuilder();
        condition.and(letterBox.userId.eq(userId));

        if (boxType != BoxType.NONE) {
            condition.and(letterBox.boxType.eq(boxType));
        }

        return condition;
    }

    private BooleanBuilder buildDeletionCondition(Long userId, List<Long> letterIds, LetterType letterType,
                                                  BoxType boxType) {
        QLetterBoxEntity letterBox = QLetterBoxEntity.letterBoxEntity;
        BooleanBuilder condition = new BooleanBuilder();

        if (userId != null) {
            condition.and(letterBox.userId.eq(userId));
        }
        if (!letterIds.isEmpty()) {
            condition.and(letterBox.letterId.in(letterIds));
        }
        if (letterType != LetterType.NONE) {
            condition.and(letterBox.letterType.eq(letterType));
        }
        if (boxType != BoxType.NONE) {
            condition.and(letterBox.boxType.eq(boxType));
        }

        return condition;
    }
}
