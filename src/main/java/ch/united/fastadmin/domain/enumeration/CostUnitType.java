package ch.united.fastadmin.domain.enumeration;

/**
 * The CostUnitType enumeration.
 */
public enum CostUnitType {
    PRODUCTIVE("P"),
    NOT_PRODUCTIVE("U");

    private final String value;

    CostUnitType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
