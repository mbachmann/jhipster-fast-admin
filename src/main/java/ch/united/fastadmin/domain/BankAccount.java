package ch.united.fastadmin.domain;

import ch.united.fastadmin.domain.enumeration.AutoSynch;
import ch.united.fastadmin.domain.enumeration.AutoSynchDirection;
import ch.united.fastadmin.domain.enumeration.Currency;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The owner company bank account
 */
@Entity
@Table(name = "bank_account")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BankAccount implements Serializable {

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
     * whether it is a default bank account
     */
    @Column(name = "default_bank_account")
    private Boolean defaultBankAccount;

    /**
     * The account description
     */
    @Column(name = "description")
    private String description;

    /**
     * The Bank Name
     */
    @Column(name = "bank_name")
    private String bankName;

    /**
     * Account Number
     */
    @Column(name = "number")
    private String number;

    /**
     * IBAN Number
     */
    @Column(name = "iban")
    private String iban;

    /**
     * BIC/SWIFT Code
     */
    @Column(name = "bic")
    private String bic;

    /**
     * bank’s postal account
     */
    @Column(name = "post_account")
    private String postAccount;

    /**
     * one of: [active, inactive, requested]
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "auto_sync")
    private AutoSynch autoSync;

    /**
     * [in, out, null (means no direction specified)]
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "auto_sync_direction")
    private AutoSynchDirection autoSyncDirection;

    /**
     * ISO currency name o
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "currency")
    private Currency currency;

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

    public BankAccount id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getRemoteId() {
        return this.remoteId;
    }

    public BankAccount remoteId(Integer remoteId) {
        this.remoteId = remoteId;
        return this;
    }

    public void setRemoteId(Integer remoteId) {
        this.remoteId = remoteId;
    }

    public Boolean getDefaultBankAccount() {
        return this.defaultBankAccount;
    }

    public BankAccount defaultBankAccount(Boolean defaultBankAccount) {
        this.defaultBankAccount = defaultBankAccount;
        return this;
    }

    public void setDefaultBankAccount(Boolean defaultBankAccount) {
        this.defaultBankAccount = defaultBankAccount;
    }

    public String getDescription() {
        return this.description;
    }

    public BankAccount description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBankName() {
        return this.bankName;
    }

    public BankAccount bankName(String bankName) {
        this.bankName = bankName;
        return this;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getNumber() {
        return this.number;
    }

    public BankAccount number(String number) {
        this.number = number;
        return this;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getIban() {
        return this.iban;
    }

    public BankAccount iban(String iban) {
        this.iban = iban;
        return this;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getBic() {
        return this.bic;
    }

    public BankAccount bic(String bic) {
        this.bic = bic;
        return this;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public String getPostAccount() {
        return this.postAccount;
    }

    public BankAccount postAccount(String postAccount) {
        this.postAccount = postAccount;
        return this;
    }

    public void setPostAccount(String postAccount) {
        this.postAccount = postAccount;
    }

    public AutoSynch getAutoSync() {
        return this.autoSync;
    }

    public BankAccount autoSync(AutoSynch autoSync) {
        this.autoSync = autoSync;
        return this;
    }

    public void setAutoSync(AutoSynch autoSync) {
        this.autoSync = autoSync;
    }

    public AutoSynchDirection getAutoSyncDirection() {
        return this.autoSyncDirection;
    }

    public BankAccount autoSyncDirection(AutoSynchDirection autoSyncDirection) {
        this.autoSyncDirection = autoSyncDirection;
        return this;
    }

    public void setAutoSyncDirection(AutoSynchDirection autoSyncDirection) {
        this.autoSyncDirection = autoSyncDirection;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public BankAccount currency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Boolean getInactiv() {
        return this.inactiv;
    }

    public BankAccount inactiv(Boolean inactiv) {
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
        if (!(o instanceof BankAccount)) {
            return false;
        }
        return id != null && id.equals(((BankAccount) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BankAccount{" +
            "id=" + getId() +
            ", remoteId=" + getRemoteId() +
            ", defaultBankAccount='" + getDefaultBankAccount() + "'" +
            ", description='" + getDescription() + "'" +
            ", bankName='" + getBankName() + "'" +
            ", number='" + getNumber() + "'" +
            ", iban='" + getIban() + "'" +
            ", bic='" + getBic() + "'" +
            ", postAccount='" + getPostAccount() + "'" +
            ", autoSync='" + getAutoSync() + "'" +
            ", autoSyncDirection='" + getAutoSyncDirection() + "'" +
            ", currency='" + getCurrency() + "'" +
            ", inactiv='" + getInactiv() + "'" +
            "}";
    }
}
