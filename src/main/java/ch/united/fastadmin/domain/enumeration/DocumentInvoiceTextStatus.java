package ch.united.fastadmin.domain.enumeration;

/**
 * The DocumentInvoiceTextStatus enumeration.
 */
public enum DocumentInvoiceTextStatus {
    DEFAULT("D"),
    PAYMENT_REMINDER("R"),
    FIRST_REMINDER("R1"),
    SECOND_REMINDER("R2"),
    THIRD_REMINDER("R3"),
    DEBT_COLLECTION("DC"),
    RECEIPT("RE");

    private final String value;

    DocumentInvoiceTextStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
