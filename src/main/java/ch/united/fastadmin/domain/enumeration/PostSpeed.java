package ch.united.fastadmin.domain.enumeration;

/**
 * The PostSpeed enumeration.
 */
public enum PostSpeed {
    PRIORIRY("P"),
    ECONOMY("E");

    private final String value;

    PostSpeed(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
