package ch.united.fastadmin.domain.enumeration;

/**
 * The ProjectStatus enumeration.
 */
public enum ProjectStatus {
    OPEN("O"),
    CLOSED("C"),
    BILLED("B"),
    PENDING("P");

    private final String value;

    ProjectStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
