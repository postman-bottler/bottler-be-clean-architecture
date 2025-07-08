package online.bottler.letter.application.port.in;

public interface RecentReplyForLetterUseCase {
    void push(Long receiverId, Long id, String label);

    void delete(Long receiverId, Long id, String label);
}
