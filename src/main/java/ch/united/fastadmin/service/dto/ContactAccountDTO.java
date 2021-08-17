package ch.united.fastadmin.service.dto;

import ch.united.fastadmin.domain.enumeration.AccountType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ch.united.fastadmin.domain.ContactAccount} entity.
 */
@ApiModel(description = "Bank or Postfinance account of the Contact")
public class ContactAccountDTO implements Serializable {

    private Long id;

    /**
     * id of a remote system
     */

    @ApiModelProperty(value = "id of a remote system")
    private Integer remoteId;

    /**
     * whether it is a default contact's account ,
     */
    @ApiModelProperty(value = "whether it is a default contact's account ,")
    private Boolean defaultAccount;

    /**
     * type (possible values: IBAN, ISR) ,
     */
    @ApiModelProperty(value = "type (possible values: IBAN, ISR) ,")
    private AccountType type;

    /**
     * IBAN or ISR number (depends on the type)
     */
    @ApiModelProperty(value = "IBAN or ISR number (depends on the type)")
    private String number;

    /**
     * The BIC or SWIFT number
     */
    @ApiModelProperty(value = "The BIC or SWIFT number")
    private String bic;

    /**
     * internal description
     */
    @ApiModelProperty(value = "internal description")
    private String description;

    /**
     * is not active anymore
     */
    @ApiModelProperty(value = "is not active anymore")
    private Boolean inactiv;

    private ContactDTO contact;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRemoteId() {
        return remoteId;
    }

    public void setRemoteId(Integer remoteId) {
        this.remoteId = remoteId;
    }

    public Boolean getDefaultAccount() {
        return defaultAccount;
    }

    public void setDefaultAccount(Boolean defaultAccount) {
        this.defaultAccount = defaultAccount;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getInactiv() {
        return inactiv;
    }

    public void setInactiv(Boolean inactiv) {
        this.inactiv = inactiv;
    }

    public ContactDTO getContact() {
        return contact;
    }

    public void setContact(ContactDTO contact) {
        this.contact = contact;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContactAccountDTO)) {
            return false;
        }

        ContactAccountDTO contactAccountDTO = (ContactAccountDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contactAccountDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactAccountDTO{" +
            "id=" + getId() +
            ", remoteId=" + getRemoteId() +
            ", defaultAccount='" + getDefaultAccount() + "'" +
            ", type='" + getType() + "'" +
            ", number='" + getNumber() + "'" +
            ", bic='" + getBic() + "'" +
            ", description='" + getDescription() + "'" +
            ", inactiv='" + getInactiv() + "'" +
            ", contact=" + getContact() +
            "}";
    }
}
