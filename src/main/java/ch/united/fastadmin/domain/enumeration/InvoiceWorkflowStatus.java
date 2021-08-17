package ch.united.fastadmin.domain.enumeration;

/**
 * The InvoiceWorkflowStatus enumeration.
 */
public enum InvoiceWorkflowStatus {
    PAYMENT_REMINDER("R"),
    FIRST_REMINDER("R1"),
    SECOND_REMINDER("R2"),
    THIRD_REMINDER("R3");

    private final String value;

    InvoiceWorkflowStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
