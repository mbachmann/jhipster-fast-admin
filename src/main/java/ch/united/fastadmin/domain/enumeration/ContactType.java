package ch.united.fastadmin.domain.enumeration;

/**
 * The ContactType enumeration.
 */
public enum ContactType {
    COMPANY("M"),
    PRIVATE("F");

    private final String value;

    ContactType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
