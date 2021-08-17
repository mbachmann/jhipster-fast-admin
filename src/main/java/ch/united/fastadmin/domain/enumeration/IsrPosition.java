package ch.united.fastadmin.domain.enumeration;

/**
 * The IsrPosition enumeration.
 */
public enum IsrPosition {
    ADDITIONAL_PAGE("A"),
    FIRST_PAGE("F"),
    LAST_PAGE("L");

    private final String value;

    IsrPosition(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
