package ch.united.fastadmin.domain.enumeration;

/**
 * The AccountType enumeration.
 */
public enum AccountType {
    IBAN_NUMBER("IBAN"),
    ORANGE_IMPAYMENT("ISR"),
    QR_CODE("QR"),
    QR_CODE_WITH_REFERENCE("QRREF");

    private final String value;

    AccountType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
