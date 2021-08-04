package ch.united.fastadmin.domain.enumeration;

/**
 * The GenderType enumeration.
 */
public enum GenderType {
    MALE("M"),
    FEMALE("F"),
    OTHER("O");

    private final String value;

    GenderType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
