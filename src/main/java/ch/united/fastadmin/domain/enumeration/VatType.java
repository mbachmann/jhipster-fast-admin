package ch.united.fastadmin.domain.enumeration;

/**
 * The VatType enumeration.
 */
public enum VatType {
    PERCENT("P"),
    NO_VAT("N"),
    FREE_INPUT("F");

    private final String value;

    VatType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
