package ch.united.fastadmin.domain.enumeration;

/**
 * The ReportingEntityType enumeration.
 */
public enum ReportingEntityType {
    PROJECT("P"),
    COST_UNIT("CU"),
    CONTACT("C");

    private final String value;

    ReportingEntityType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
