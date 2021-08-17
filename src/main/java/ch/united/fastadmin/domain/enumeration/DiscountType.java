package ch.united.fastadmin.domain.enumeration;

/**
 * The DiscountType enumeration.
 */
public enum DiscountType {
    IN_PERCENT("P"),
    AMOUNT("A");

    private final String value;

    DiscountType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
