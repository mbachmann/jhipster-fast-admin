package ch.united.fastadmin.domain.enumeration;

/**
 * The InvoiceStatus enumeration.
 */
public enum InvoiceStatus {
    DRAFT("DR"),
    SENT("S"),
    PAYED("P"),
    PARTIALLY_PAID("PP"),
    PAYMENT_REMINDER("R"),
    FIRST_REMINDER("R1"),
    SECOND_REMINDER("R2"),
    THIRD_REMINDER("R3"),
    DEBT_COLLECTION("DC"),
    CANCELD("C"),
    DELETED("D");

    private final String value;

    InvoiceStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
