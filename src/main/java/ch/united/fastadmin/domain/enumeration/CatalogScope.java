package ch.united.fastadmin.domain.enumeration;

/**
 * The CatalogScope enumeration.
 */
public enum CatalogScope {
    SERVICE("S"),
    PRODUCT("P"),
    ALL("A");

    private final String value;

    CatalogScope(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
