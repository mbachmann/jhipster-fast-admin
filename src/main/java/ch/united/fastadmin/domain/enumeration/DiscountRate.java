package ch.united.fastadmin.domain.enumeration;

/**
 * The DiscountRate enumeration.
 */
public enum DiscountRate {
    IN_PERCENT("P"),
    AMOUNT("A");

    private final String value;

    DiscountRate(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
