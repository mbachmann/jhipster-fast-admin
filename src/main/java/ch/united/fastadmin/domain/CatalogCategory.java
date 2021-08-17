package ch.united.fastadmin.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Category for Products or Services
 */
@Entity
@Table(name = "catalog_category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CatalogCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * id of a remote system
     */

    @Column(name = "remote_id", unique = true)
    private Integer remoteId;

    /**
     * Category name
     */
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "accounting_account_number")
    private String accountingAccountNumber;

    /**
     * how many catalog items are using this category
     */
    @Column(name = "fa_usage")
    private Integer usage;

    /**
     * is not active anymore
     */
    @Column(name = "inactiv")
    private Boolean inactiv;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CatalogCategory id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getRemoteId() {
        return this.remoteId;
    }

    public CatalogCategory remoteId(Integer remoteId) {
        this.remoteId = remoteId;
        return this;
    }

    public void setRemoteId(Integer remoteId) {
        this.remoteId = remoteId;
    }

    public String getName() {
        return this.name;
    }

    public CatalogCategory name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountingAccountNumber() {
        return this.accountingAccountNumber;
    }

    public CatalogCategory accountingAccountNumber(String accountingAccountNumber) {
        this.accountingAccountNumber = accountingAccountNumber;
        return this;
    }

    public void setAccountingAccountNumber(String accountingAccountNumber) {
        this.accountingAccountNumber = accountingAccountNumber;
    }

    public Integer getUsage() {
        return this.usage;
    }

    public CatalogCategory usage(Integer usage) {
        this.usage = usage;
        return this;
    }

    public void setUsage(Integer usage) {
        this.usage = usage;
    }

    public Boolean getInactiv() {
        return this.inactiv;
    }

    public CatalogCategory inactiv(Boolean inactiv) {
        this.inactiv = inactiv;
        return this;
    }

    public void setInactiv(Boolean inactiv) {
        this.inactiv = inactiv;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CatalogCategory)) {
            return false;
        }
        return id != null && id.equals(((CatalogCategory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CatalogCategory{" +
            "id=" + getId() +
            ", remoteId=" + getRemoteId() +
            ", name='" + getName() + "'" +
            ", accountingAccountNumber='" + getAccountingAccountNumber() + "'" +
            ", usage=" + getUsage() +
            ", inactiv='" + getInactiv() + "'" +
            "}";
    }
}
