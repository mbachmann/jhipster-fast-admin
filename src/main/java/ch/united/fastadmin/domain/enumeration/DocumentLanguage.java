package ch.united.fastadmin.domain.enumeration;

/**
 * The DocumentLanguage enumeration.
 */
public enum DocumentLanguage {
    FRENCH("fr"),
    ENGLISH("en"),
    GERMAN("de"),
    SPANISH("es"),
    ITALIAN("it");

    private final String value;

    DocumentLanguage(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
