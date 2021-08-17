package ch.united.fastadmin.domain.enumeration;

/**
 * The DocumentTextType enumeration.
 */
public enum DocumentTextType {
    DOCUMENT("D"),
    EMAIL("E");

    private final String value;

    DocumentTextType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
