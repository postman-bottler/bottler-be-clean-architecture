package online.bottler.letter.application.port.in;

public interface RecentReplyForLetterUseCase {

    void push(Long id, String label, Long receiverId);

    void delete(Long receiverId, Long id, String label);
}
