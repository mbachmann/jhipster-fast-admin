package ch.united.fastadmin.domain.enumeration;

/**
 * The WorkflowAction enumeration.
 */
public enum WorkflowAction {
    REMIND_ME("M"),
    REMIND_CONTACT_BY_EMAIL("CE"),
    REMIND_CONTACT_BY_POST("CP");

    private final String value;

    WorkflowAction(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
