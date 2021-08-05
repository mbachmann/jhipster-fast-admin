package ch.united.fastadmin.domain.enumeration;

/**
 * The IntervalType enumeration.
 */
public enum IntervalType {
    HOUR("hour"),
    DAY("day"),
    MONTH("month");

    private final String value;

    IntervalType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
