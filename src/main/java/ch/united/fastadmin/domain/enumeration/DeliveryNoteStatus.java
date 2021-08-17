package ch.united.fastadmin.domain.enumeration;

/**
 * The DeliveryNoteStatus enumeration.
 */
public enum DeliveryNoteStatus {
    DRAFT("DR"),
    SENT("S"),
    BILLED("B"),
    DELETED("D");

    private final String value;

    DeliveryNoteStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
