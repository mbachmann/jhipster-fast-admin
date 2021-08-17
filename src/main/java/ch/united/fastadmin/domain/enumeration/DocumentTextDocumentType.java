package ch.united.fastadmin.domain.enumeration;

/**
 * The DocumentTextDocumentType enumeration.
 */
public enum DocumentTextDocumentType {
    OFFER("O"),
    ORDER_CONFIRMATION("C"),
    DELIVERY_NOTE("D"),
    INVOICE("I");

    private final String value;

    DocumentTextDocumentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
