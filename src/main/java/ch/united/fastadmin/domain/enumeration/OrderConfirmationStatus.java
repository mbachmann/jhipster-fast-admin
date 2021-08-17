package ch.united.fastadmin.domain.enumeration;

/**
 * The OrderConfirmationStatus enumeration.
 */
public enum OrderConfirmationStatus {
    DRAFT("DR"),
    SENT("S"),
    BILLED("B"),
    DELETED("D");

    private final String value;

    OrderConfirmationStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
