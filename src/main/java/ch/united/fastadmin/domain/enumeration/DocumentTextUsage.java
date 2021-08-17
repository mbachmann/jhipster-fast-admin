package ch.united.fastadmin.domain.enumeration;

/**
 * The DocumentTextUsage enumeration.
 */
public enum DocumentTextUsage {
    TITLE("T"),
    INTRODUCTION("I"),
    CONDITION("C");

    private final String value;

    DocumentTextUsage(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
