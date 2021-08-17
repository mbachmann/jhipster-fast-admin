package ch.united.fastadmin.domain.enumeration;

/**
 * The DocumentPositionType enumeration.
 */
public enum DocumentPositionType {
    NORMAL("N"),
    TEXT("T"),
    PAGE_BREAK("PB"),
    HEADER("H"),
    SUBTOTAL_INCREMENTAL("SI"),
    SUBTOTAL_NON_INCREMENTAL("SNI");

    private final String value;

    DocumentPositionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
