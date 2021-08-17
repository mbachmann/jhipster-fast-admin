package ch.united.fastadmin.domain.enumeration;

/**
 * The IsrType enumeration.
 */
public enum IsrType {
    RIS,
    IBAN,
    ISR,
    QR,
    QRREF;

    private final String value;

    IsrType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
