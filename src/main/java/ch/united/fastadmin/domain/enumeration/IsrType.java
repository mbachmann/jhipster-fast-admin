package ch.united.fastadmin.domain.enumeration;

/**
 * The IsrType enumeration.
 */
public enum IsrType {
    RED_INPAYMENT("RIS"),
    IBAN_NUMBER("IBAN"),
    ORANGE_INPAYMENT("ISR"),
    ORANGE_INPAYMENT_PLUS("ISR_PLUS"),
    QR_CODE("QR"),
    QR_CODE_WITH_REFERENCE("QRREF");

    private final String value;

    IsrType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
