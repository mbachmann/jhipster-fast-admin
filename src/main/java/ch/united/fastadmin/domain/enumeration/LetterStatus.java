package ch.united.fastadmin.domain.enumeration;

/**
 * The LetterStatus enumeration.
 */
public enum LetterStatus {
    DRAFT("DR"),
    SENT("S"),
    DELETED("D");

    private final String value;

    LetterStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
