package ch.united.fastadmin.domain.enumeration;

/**
 * The AccountType enumeration.
 */
public enum AccountType {
    IBAN,
    ISR,
    QR,
    QRREF;

    private final String value;

    AccountType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
