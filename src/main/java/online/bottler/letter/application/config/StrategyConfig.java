package online.bottler.letter.application.config;

import java.util.Map;
import online.bottler.letter.application.strategy.LetterDeleteStrategy;
import online.bottler.letter.application.strategy.LetterDeleteStrategyReceive;
import online.bottler.letter.application.strategy.LetterDeleteStrategySend;
import online.bottler.letter.application.strategy.ReplyLetterDeleteStrategyReceive;
import online.bottler.letter.application.strategy.ReplyLetterDeleteStrategySend;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.LetterBoxType;
import online.bottler.letter.domain.LetterType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StrategyConfig {

    @Bean
    public Map<LetterBoxType, LetterDeleteStrategy> strategyMap(
            LetterDeleteStrategySend letterDeleteStrategySend,
            LetterDeleteStrategyReceive letterDeleteStrategyReceive,
            ReplyLetterDeleteStrategySend replyLetterDeleteStrategySend,
            ReplyLetterDeleteStrategyReceive replyLetterDeleteStrategyReceive) {

        return Map.of(
                LetterBoxType.of(LetterType.LETTER, BoxType.SEND), letterDeleteStrategySend,
                LetterBoxType.of(LetterType.LETTER, BoxType.RECEIVE), letterDeleteStrategyReceive,
                LetterBoxType.of(LetterType.REPLY_LETTER, BoxType.SEND), replyLetterDeleteStrategySend,
                LetterBoxType.of(LetterType.REPLY_LETTER, BoxType.RECEIVE), replyLetterDeleteStrategyReceive
        );
    }
}
