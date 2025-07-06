package online.bottler.letter.adapter.out.persistence.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import online.bottler.letter.adapter.out.persistence.entity.QLetterBoxEntity;
import online.bottler.letter.adapter.out.persistence.entity.QLetterBoxTypeEntity;
import online.bottler.letter.adapter.out.persistence.entity.QLetterContentEntity;
import online.bottler.letter.adapter.out.persistence.entity.QLetterEntity;
import online.bottler.letter.adapter.out.persistence.entity.QReplyLetterEntity;
import online.bottler.letter.adapter.out.persistence.model.LetterSummaryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.LetterType;

@Repository
@RequiredArgsConstructor
public class LetterBoxQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Page<LetterSummaryProjection> fetchLetterSummariesByUserIdAndBoxType(Long userId, BoxType boxType, Pageable pageable) {
        QLetterBoxEntity letterBox = QLetterBoxEntity.letterBoxEntity;
        QLetterBoxTypeEntity letterBoxType = letterBox.letterBoxTypeEntity;

        QLetterEntity letter = QLetterEntity.letterEntity;

        QReplyLetterEntity replyLetter = QReplyLetterEntity.replyLetterEntity;


        StringExpression letterTitle = getLetterTitle(letterBox, letter, replyLetter);
        StringExpression letterLabel = getLetterLabel(letterBox, letter, replyLetter);

        BooleanBuilder condition = buildFetchCondition(userId, boxType);

        List<LetterSummaryProjection> letterSummaryProjections = queryFactory
                .select(Projections.constructor(
                        LetterSummaryProjection.class,
                        letterBox.letterId,
                        letterTitle,
                        letterLabel,
                        letterBoxType.letterType,
                        letterBoxType.boxType,
                        letterBox.createdAt
                ))
                .from(letterBox)
                .leftJoin(letter).on(letterBox.letterId.eq(letter.id)
                        .and(letterBoxType.letterType.eq(LetterType.LETTER)))
                .leftJoin(replyLetter).on(letterBox.letterId.eq(replyLetter.id)
                        .and(letterBoxType.letterType.eq(LetterType.REPLY_LETTER)))
                .where(condition)
                .orderBy(letterBox.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = countLetters(userId, boxType);

        return new PageImpl<>(letterSummaryProjections, pageable, total);
    }

    public long countLetters(Long userId, BoxType boxType) {
        QLetterBoxEntity letterBox = QLetterBoxEntity.letterBoxEntity;
        BooleanBuilder condition = buildFetchCondition(userId, boxType);

        Long count = queryFactory
                .select(letterBox.id.count())
                .from(letterBox)
                .where(condition)
                .fetchOne();
        return count != null ? count : 0L;
    }

    public void deleteLetters(Long userId, List<Long> letterIds, LetterType letterType, BoxType boxType) {
        QLetterBoxEntity letterBox = QLetterBoxEntity.letterBoxEntity;
        BooleanBuilder condition = buildDeletionCondition(userId, letterIds, letterType, boxType, letterBox);

        queryFactory
                .delete(letterBox)
                .where(condition)
                .execute();
    }

    public void deleteByCondition(List<Long> letterIds, LetterType letterType, BoxType boxType) {
        QLetterBoxEntity letterBox = QLetterBoxEntity.letterBoxEntity;
        BooleanBuilder condition = buildDeletionCondition(null, letterIds, letterType, boxType, letterBox);

        queryFactory
                .delete(letterBox)
                .where(condition)
                .execute();
    }

    private StringExpression getLetterTitle(QLetterBoxEntity letterBox, QLetterEntity letter,
                                            QReplyLetterEntity replyLetter) {
        QLetterBoxTypeEntity letterBoxType = letterBox.letterBoxTypeEntity;
        QLetterContentEntity letterContent = letter.letterContentEntity;
        QLetterContentEntity replyLetterContent = replyLetter.letterContentEntity;
        return new CaseBuilder()
                .when(letterBoxType.letterType.eq(LetterType.LETTER)).then(letterContent.title)
                .when(letterBoxType.letterType.eq(LetterType.REPLY_LETTER)).then(replyLetterContent.title)
                .otherwise("Unknown Title");
    }

    private StringExpression getLetterLabel(QLetterBoxEntity letterBox, QLetterEntity letter,
                                            QReplyLetterEntity replyLetter) {
        QLetterBoxTypeEntity letterBoxType = letterBox.letterBoxTypeEntity;
        QLetterContentEntity letterContent = letter.letterContentEntity;
        QLetterContentEntity replyLetterContent = replyLetter.letterContentEntity;
        return new CaseBuilder()
                .when(letterBoxType.letterType.eq(LetterType.LETTER)).then(letterContent.label)
                .when(letterBoxType.letterType.eq(LetterType.REPLY_LETTER)).then(replyLetterContent.label)
                .otherwise("Unknown Label");
    }

    private BooleanBuilder buildFetchCondition(Long userId, BoxType boxType) {
        QLetterBoxEntity letterBox = QLetterBoxEntity.letterBoxEntity;
        QLetterBoxTypeEntity letterBoxType = letterBox.letterBoxTypeEntity;
        BooleanBuilder condition = new BooleanBuilder();
        condition.and(letterBox.userId.eq(userId));

        if (boxType != null) {
            condition.and(letterBoxType.boxType.eq(boxType));
        }

        return condition;
    }

    private BooleanBuilder buildDeletionCondition(Long userId, List<Long> letterIds, LetterType letterType,
                                                  BoxType boxType, QLetterBoxEntity letterBox) {
        QLetterBoxTypeEntity letterBoxType = letterBox.letterBoxTypeEntity;
        BooleanBuilder condition = new BooleanBuilder();

        if (userId != null) {
            condition.and(letterBox.userId.eq(userId));
        }
        if (letterIds != null) {
            condition.and(letterBox.letterId.in(letterIds));
        }
        if (letterType != null) {
            condition.and(letterBoxType.letterType.eq(letterType));
        }
        if (boxType != null) {
            condition.and(letterBoxType.boxType.eq(boxType));
        }

        return condition;
    }
}
