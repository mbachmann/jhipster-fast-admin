package ch.united.fastadmin.domain.enumeration;

/**
 * The CommunicationNewsletter enumeration.
 */
public enum CommunicationNewsletter {
    SEND_ADDRESS_AND_CONTACTS("A"),
    SEND_TO_MAIN_CONTACT_ONLY("M"),
    NO_NEWS_LETTER("N");

    private final String value;

    CommunicationNewsletter(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
