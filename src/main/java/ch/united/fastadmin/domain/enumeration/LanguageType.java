package ch.united.fastadmin.domain.enumeration;

/**
 * The LanguageType enumeration.
 */
public enum LanguageType {
    FRENCH("fr"),
    ENGLISH("en"),
    GERMAN("de");

    private final String value;

    LanguageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
