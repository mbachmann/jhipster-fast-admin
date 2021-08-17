package ch.united.fastadmin.domain.enumeration;

/**
 * The OfferAcceptOnlineStatus enumeration.
 */
public enum OfferAcceptOnlineStatus {
    ACCEPTED("A"),
    DECLINED("D"),
    UNKNOWN("U");

    private final String value;

    OfferAcceptOnlineStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
