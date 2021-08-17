package ch.united.fastadmin.domain.enumeration;

/**
 * The CommunicationChannel enumeration.
 */
public enum CommunicationChannel {
    NO_PREFERENCE("U"),
    BY_EMAIL("E"),
    BY_POST("P");

    private final String value;

    CommunicationChannel(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
