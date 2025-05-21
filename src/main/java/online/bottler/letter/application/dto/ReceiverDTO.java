package online.bottler.letter.application.dto;

import online.bottler.letter.domain.Letter;

public record ReceiverDTO(Long receiverId, String title) {
    public static ReceiverDTO from(Letter letter) {
        return new ReceiverDTO(letter.getUserId(), letter.getTitle());
    }
}
