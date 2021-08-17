package ch.united.fastadmin.service.dto;

import ch.united.fastadmin.domain.enumeration.AutoSynch;
import ch.united.fastadmin.domain.enumeration.AutoSynchDirection;
import ch.united.fastadmin.domain.enumeration.Currency;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ch.united.fastadmin.domain.BankAccount} entity.
 */
@ApiModel(description = "The owner company bank account")
public class BankAccountDTO implements Serializable {

    private Long id;

    /**
     * id of a remote system
     */

    @ApiModelProperty(value = "id of a remote system")
    private Integer remoteId;

    /**
     * whether it is a default bank account
     */
    @ApiModelProperty(value = "whether it is a default bank account")
    private Boolean defaultBankAccount;

    /**
     * The account description
     */
    @ApiModelProperty(value = "The account description")
    private String description;

    /**
     * The Bank Name
     */
    @ApiModelProperty(value = "The Bank Name")
    private String bankName;

    /**
     * Account Number
     */
    @ApiModelProperty(value = "Account Number")
    private String number;

    /**
     * IBAN Number
     */
    @ApiModelProperty(value = "IBAN Number")
    private String iban;

    /**
     * BIC/SWIFT Code
     */
    @ApiModelProperty(value = "BIC/SWIFT Code")
    private String bic;

    /**
     * bank’s postal account
     */
    @ApiModelProperty(value = "bank’s postal account")
    private String postAccount;

    /**
     * one of: [active, inactive, requested]
     */
    @ApiModelProperty(value = "one of: [active, inactive, requested]")
    private AutoSynch autoSync;

    /**
     * [in, out, null (means no direction specified)]
     */
    @ApiModelProperty(value = "[in, out, null (means no direction specified)]")
    private AutoSynchDirection autoSyncDirection;

    /**
     * ISO currency name o
     */
    @ApiModelProperty(value = "ISO currency name o")
    private Currency currency;

    /**
     * is not active anymore
     */
    @ApiModelProperty(value = "is not active anymore")
    private Boolean inactiv;

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

    public Boolean getDefaultBankAccount() {
        return defaultBankAccount;
    }

    public void setDefaultBankAccount(Boolean defaultBankAccount) {
        this.defaultBankAccount = defaultBankAccount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public String getPostAccount() {
        return postAccount;
    }

    public void setPostAccount(String postAccount) {
        this.postAccount = postAccount;
    }

    public AutoSynch getAutoSync() {
        return autoSync;
    }

    public void setAutoSync(AutoSynch autoSync) {
        this.autoSync = autoSync;
    }

    public AutoSynchDirection getAutoSyncDirection() {
        return autoSyncDirection;
    }

    public void setAutoSyncDirection(AutoSynchDirection autoSyncDirection) {
        this.autoSyncDirection = autoSyncDirection;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Boolean getInactiv() {
        return inactiv;
    }

    public void setInactiv(Boolean inactiv) {
        this.inactiv = inactiv;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BankAccountDTO)) {
            return false;
        }

        BankAccountDTO bankAccountDTO = (BankAccountDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, bankAccountDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BankAccountDTO{" +
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
