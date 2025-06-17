package online.bottler.letter.application.config;

import java.util.Map;
import online.bottler.letter.application.strategy.LetterDeleteStrategy;
import online.bottler.letter.application.strategy.LetterDeleteStrategyReceive;
import online.bottler.letter.application.strategy.LetterDeleteStrategySend;
import online.bottler.letter.application.strategy.ReplyLetterDeleteStrategyReceive;
import online.bottler.letter.application.strategy.ReplyLetterDeleteStrategySend;
import online.bottler.letter.domain.BoxType;
import online.bottler.letter.domain.LetterDeleteKey;
import online.bottler.letter.domain.LetterType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StrategyConfig {

    @Bean
    public Map<LetterDeleteKey, LetterDeleteStrategy> strategyMap(
            LetterDeleteStrategySend letterDeleteStrategySend,
            LetterDeleteStrategyReceive letterDeleteStrategyReceive,
            ReplyLetterDeleteStrategySend replyLetterDeleteStrategySend,
            ReplyLetterDeleteStrategyReceive replyLetterDeleteStrategyReceive) {

        return Map.of(
                LetterDeleteKey.of(LetterType.LETTER, BoxType.SEND), letterDeleteStrategySend,
                LetterDeleteKey.of(LetterType.LETTER, BoxType.RECEIVE), letterDeleteStrategyReceive,
                LetterDeleteKey.of(LetterType.REPLY_LETTER, BoxType.SEND), replyLetterDeleteStrategySend,
                LetterDeleteKey.of(LetterType.REPLY_LETTER, BoxType.RECEIVE), replyLetterDeleteStrategyReceive
        );
    }
}
