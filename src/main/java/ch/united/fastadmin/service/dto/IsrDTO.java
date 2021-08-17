package ch.united.fastadmin.service.dto;

import ch.united.fastadmin.domain.enumeration.IsrPosition;
import ch.united.fastadmin.domain.enumeration.IsrType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link ch.united.fastadmin.domain.Isr} entity.
 */
@ApiModel(
    description = "ISR definition for ISR - orange inpayment slip, ISR+ - orange inpayment slip plus, RIS - red inpayment slip, QR Code and QR Code with reference"
)
public class IsrDTO implements Serializable {

    private Long id;

    /**
     * whether ISR is a default one
     */
    @ApiModelProperty(value = "whether ISR is a default one")
    private Boolean defaultIsr;

    /**
     * SR type; possible values: ISR - orange inpayment slip, ISR+ - orange inpayment slip plus, RIS - red inpayment slip, QR Code and QR Code with reference
     */
    @ApiModelProperty(
        value = "SR type; possible values: ISR - orange inpayment slip, ISR+ - orange inpayment slip plus, RIS - red inpayment slip, QR Code and QR Code with reference"
    )
    private IsrType type;

    /**
     * ISR position; possible values: A - additional page, F - first page, L - last page
     */
    @ApiModelProperty(value = "ISR position; possible values: A - additional page, F - first page, L - last page")
    private IsrPosition position;

    /**
     * optional ISR name (for internal system use only)
     */
    @ApiModelProperty(value = "optional ISR name (for internal system use only)")
    private String name;

    /**
     * Name of the bank
     */
    @ApiModelProperty(value = "Name of the bank")
    private String bankName;

    /**
     * Address of the bank
     */
    @ApiModelProperty(value = "Address of the bank")
    private String bankAddress;

    /**
     * name of the recipient
     */
    @ApiModelProperty(value = "name of the recipient")
    private String recipientName;

    /**
     * additional information of the recipient
     */
    @ApiModelProperty(value = "additional information of the recipient")
    private String recipientAddition;

    /**
     * street of the recipient
     */
    @ApiModelProperty(value = "street of the recipient")
    private String recipientStreet;

    /**
     * city of the recipient
     */
    @ApiModelProperty(value = "city of the recipient")
    private String recipientCity;

    /**
     * the number (BESR or REF)
     */
    @ApiModelProperty(value = "the number (BESR or REF)")
    private String deliveryNumber;

    /**
     * the IBAN account number
     */
    @ApiModelProperty(value = "the IBAN account number")
    private String iban;

    /**
     * the subscriber number (Teilnehmernummer)
     */
    @ApiModelProperty(value = "the subscriber number (Teilnehmernummer)")
    private String subscriberNumber;

    /**
     * left print adjust in mm
     */
    @ApiModelProperty(value = "left print adjust in mm")
    private Double leftPrintAdjust;

    /**
     * top print adjust in mm
     */
    @ApiModelProperty(value = "top print adjust in mm")
    private Double topPrintAdjust;

    private ZonedDateTime created;

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

    public Boolean getDefaultIsr() {
        return defaultIsr;
    }

    public void setDefaultIsr(Boolean defaultIsr) {
        this.defaultIsr = defaultIsr;
    }

    public IsrType getType() {
        return type;
    }

    public void setType(IsrType type) {
        this.type = type;
    }

    public IsrPosition getPosition() {
        return position;
    }

    public void setPosition(IsrPosition position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientAddition() {
        return recipientAddition;
    }

    public void setRecipientAddition(String recipientAddition) {
        this.recipientAddition = recipientAddition;
    }

    public String getRecipientStreet() {
        return recipientStreet;
    }

    public void setRecipientStreet(String recipientStreet) {
        this.recipientStreet = recipientStreet;
    }

    public String getRecipientCity() {
        return recipientCity;
    }

    public void setRecipientCity(String recipientCity) {
        this.recipientCity = recipientCity;
    }

    public String getDeliveryNumber() {
        return deliveryNumber;
    }

    public void setDeliveryNumber(String deliveryNumber) {
        this.deliveryNumber = deliveryNumber;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getSubscriberNumber() {
        return subscriberNumber;
    }

    public void setSubscriberNumber(String subscriberNumber) {
        this.subscriberNumber = subscriberNumber;
    }

    public Double getLeftPrintAdjust() {
        return leftPrintAdjust;
    }

    public void setLeftPrintAdjust(Double leftPrintAdjust) {
        this.leftPrintAdjust = leftPrintAdjust;
    }

    public Double getTopPrintAdjust() {
        return topPrintAdjust;
    }

    public void setTopPrintAdjust(Double topPrintAdjust) {
        this.topPrintAdjust = topPrintAdjust;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
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
        if (!(o instanceof IsrDTO)) {
            return false;
        }

        IsrDTO isrDTO = (IsrDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, isrDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IsrDTO{" +
            "id=" + getId() +
            ", defaultIsr='" + getDefaultIsr() + "'" +
            ", type='" + getType() + "'" +
            ", position='" + getPosition() + "'" +
            ", name='" + getName() + "'" +
            ", bankName='" + getBankName() + "'" +
            ", bankAddress='" + getBankAddress() + "'" +
            ", recipientName='" + getRecipientName() + "'" +
            ", recipientAddition='" + getRecipientAddition() + "'" +
            ", recipientStreet='" + getRecipientStreet() + "'" +
            ", recipientCity='" + getRecipientCity() + "'" +
            ", deliveryNumber='" + getDeliveryNumber() + "'" +
            ", iban='" + getIban() + "'" +
            ", subscriberNumber='" + getSubscriberNumber() + "'" +
            ", leftPrintAdjust=" + getLeftPrintAdjust() +
            ", topPrintAdjust=" + getTopPrintAdjust() +
            ", created='" + getCreated() + "'" +
            ", inactiv='" + getInactiv() + "'" +
            "}";
    }
}
