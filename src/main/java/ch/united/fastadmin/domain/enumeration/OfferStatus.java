package ch.united.fastadmin.domain.enumeration;

/**
 * The OfferStatus enumeration.
 */
public enum OfferStatus {
    DRAFT("DR"),
    SENT("S"),
    BILLED("B"),
    DELETED("D");

    private final String value;

    OfferStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
