package online.bottler.notification.adapter.out.push;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SseMessage {
    private String title;

    private String content;
}
