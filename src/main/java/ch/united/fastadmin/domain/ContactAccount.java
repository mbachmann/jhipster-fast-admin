package ch.united.fastadmin.domain;

import ch.united.fastadmin.domain.enumeration.AccountType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Bank or Postfinance account of the Contact
 */
@Entity
@Table(name = "contact_account")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ContactAccount implements Serializable {

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
     * whether it is a default contact's account ,
     */
    @Column(name = "default_account")
    private Boolean defaultAccount;

    /**
     * type (possible values: IBAN, ISR) ,
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private AccountType type;

    /**
     * IBAN or ISR number (depends on the type)
     */
    @Column(name = "number")
    private String number;

    /**
     * The BIC or SWIFT number
     */
    @Column(name = "bic")
    private String bic;

    /**
     * internal description
     */
    @Column(name = "description")
    private String description;

    /**
     * is not active anymore
     */
    @Column(name = "inactiv")
    private Boolean inactiv;

    /**
     * The contact relation
     */
    @ManyToOne
    @JsonIgnoreProperties(value = { "customFields", "relations", "groups" }, allowSetters = true)
    private Contact contact;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ContactAccount id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getRemoteId() {
        return this.remoteId;
    }

    public ContactAccount remoteId(Integer remoteId) {
        this.remoteId = remoteId;
        return this;
    }

    public void setRemoteId(Integer remoteId) {
        this.remoteId = remoteId;
    }

    public Boolean getDefaultAccount() {
        return this.defaultAccount;
    }

    public ContactAccount defaultAccount(Boolean defaultAccount) {
        this.defaultAccount = defaultAccount;
        return this;
    }

    public void setDefaultAccount(Boolean defaultAccount) {
        this.defaultAccount = defaultAccount;
    }

    public AccountType getType() {
        return this.type;
    }

    public ContactAccount type(AccountType type) {
        this.type = type;
        return this;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public String getNumber() {
        return this.number;
    }

    public ContactAccount number(String number) {
        this.number = number;
        return this;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBic() {
        return this.bic;
    }

    public ContactAccount bic(String bic) {
        this.bic = bic;
        return this;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public String getDescription() {
        return this.description;
    }

    public ContactAccount description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getInactiv() {
        return this.inactiv;
    }

    public ContactAccount inactiv(Boolean inactiv) {
        this.inactiv = inactiv;
        return this;
    }

    public void setInactiv(Boolean inactiv) {
        this.inactiv = inactiv;
    }

    public Contact getContact() {
        return this.contact;
    }

    public ContactAccount contact(Contact contact) {
        this.setContact(contact);
        return this;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContactAccount)) {
            return false;
        }
        return id != null && id.equals(((ContactAccount) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactAccount{" +
            "id=" + getId() +
            ", remoteId=" + getRemoteId() +
            ", defaultAccount='" + getDefaultAccount() + "'" +
            ", type='" + getType() + "'" +
            ", number='" + getNumber() + "'" +
            ", bic='" + getBic() + "'" +
            ", description='" + getDescription() + "'" +
            ", inactiv='" + getInactiv() + "'" +
            "}";
    }
}
