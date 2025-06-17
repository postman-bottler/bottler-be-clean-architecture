package online.bottler.letter.domain;

public record LetterDeleteKey(LetterType letterType, BoxType boxType) {
    public static LetterDeleteKey of(LetterType letterType, BoxType boxType) {
        return new LetterDeleteKey(letterType, boxType);
    }
}
