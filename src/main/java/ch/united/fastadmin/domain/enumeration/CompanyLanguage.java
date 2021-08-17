package ch.united.fastadmin.domain.enumeration;

/**
 * The CompanyLanguage enumeration.
 */
public enum CompanyLanguage {
    FRENCH("fr"),
    ENGLISH("en"),
    GERMAN("de");

    private final String value;

    CompanyLanguage(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
