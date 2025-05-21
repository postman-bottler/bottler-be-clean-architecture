package online.bottler.mapletter.exception;

public class LetterAlreadyReplyException extends RuntimeException {
    public LetterAlreadyReplyException(String message) {
        super(message);
    }
}
