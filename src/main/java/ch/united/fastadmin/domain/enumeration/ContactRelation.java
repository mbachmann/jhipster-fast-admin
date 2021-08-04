package ch.united.fastadmin.domain.enumeration;

/**
 * The ContactRelation enumeration.
 */
public enum ContactRelation {
    CUSTOMER("CL"),
    CREDITOR("CR"),
    TEAM("TE"),
    OFFICIALS("OF"),
    MEDICAL("ME"),
    OTHERS("OT");

    private final String value;

    ContactRelation(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
