package ch.united.fastadmin.service.dto;

import ch.united.fastadmin.domain.enumeration.Currency;
import ch.united.fastadmin.domain.enumeration.DiscountType;
import ch.united.fastadmin.domain.enumeration.DocumentLanguage;
import ch.united.fastadmin.domain.enumeration.OrderConfirmationStatus;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ch.united.fastadmin.domain.OrderConfirmation} entity.
 */
public class OrderConfirmationDTO implements Serializable {

    private Long id;

    /**
     * id of a remote system
     */

    @ApiModelProperty(value = "id of a remote system")
    private Integer remoteId;

    /**
     * document number
     */

    @ApiModelProperty(value = "document number")
    private String number;

    /**
     * name of a contact
     */
    @ApiModelProperty(value = "name of a contact")
    private String contactName;

    /**
     * date of the document
     */
    @ApiModelProperty(value = "date of the document")
    private LocalDate date;

    /**
     * service/delivery
     */
    @ApiModelProperty(value = "service/delivery")
    private String periodText;

    /**
     * currency (e.g. CHF, EUR)
     */
    @ApiModelProperty(value = "currency (e.g. CHF, EUR)")
    private Currency currency;

    /**
     * total amount of the delivery note
     */
    @ApiModelProperty(value = "total amount of the delivery note")
    private Double total;

    /**
     * whether prices in delivery note include VAT
     */
    @ApiModelProperty(value = "whether prices in delivery note include VAT")
    private Boolean vatIncluded;

    /**
     * optional discount rate
     */
    @ApiModelProperty(value = "optional discount rate")
    private Double discountRate;

    /**
     * optional discount type; possible values: A - amount, P - in %
     */
    @ApiModelProperty(value = "optional discount type; possible values: A - amount, P - in %")
    private DiscountType discountType;

    /**
     * language; possible values: de, en, es, fr, it ,
     */
    @ApiModelProperty(value = "language; possible values: de, en, es, fr, it ,")
    private DocumentLanguage language;

    /**
     * how many pages the document has
     */
    @ApiModelProperty(value = "how many pages the document has")
    private Integer pageAmount;

    /**
     * optional notes
     */
    @ApiModelProperty(value = "optional notes")
    private String notes;

    /**
     * status of order confirmation, possible values: DR - draft, S - sent, B - billed, D - deleted (but still visible)
     */
    @ApiModelProperty(
        value = "status of order confirmation, possible values: DR - draft, S - sent, B - billed, D - deleted (but still visible)"
    )
    private OrderConfirmationStatus status;

    private ZonedDateTime created;

    private ContactDTO contact;

    private ContactAddressDTO contactAddress;

    private ContactPersonDTO contactPerson;

    private ContactAddressDTO contactPrePageAddress;

    private LayoutDTO layout;

    private SignatureDTO signature;

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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getPeriodText() {
        return periodText;
    }

    public void setPeriodText(String periodText) {
        this.periodText = periodText;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Boolean getVatIncluded() {
        return vatIncluded;
    }

    public void setVatIncluded(Boolean vatIncluded) {
        this.vatIncluded = vatIncluded;
    }

    public Double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(Double discountRate) {
        this.discountRate = discountRate;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public DocumentLanguage getLanguage() {
        return language;
    }

    public void setLanguage(DocumentLanguage language) {
        this.language = language;
    }

    public Integer getPageAmount() {
        return pageAmount;
    }

    public void setPageAmount(Integer pageAmount) {
        this.pageAmount = pageAmount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public OrderConfirmationStatus getStatus() {
        return status;
    }

    public void setStatus(OrderConfirmationStatus status) {
        this.status = status;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public ContactDTO getContact() {
        return contact;
    }

    public void setContact(ContactDTO contact) {
        this.contact = contact;
    }

    public ContactAddressDTO getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(ContactAddressDTO contactAddress) {
        this.contactAddress = contactAddress;
    }

    public ContactPersonDTO getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(ContactPersonDTO contactPerson) {
        this.contactPerson = contactPerson;
    }

    public ContactAddressDTO getContactPrePageAddress() {
        return contactPrePageAddress;
    }

    public void setContactPrePageAddress(ContactAddressDTO contactPrePageAddress) {
        this.contactPrePageAddress = contactPrePageAddress;
    }

    public LayoutDTO getLayout() {
        return layout;
    }

    public void setLayout(LayoutDTO layout) {
        this.layout = layout;
    }

    public SignatureDTO getSignature() {
        return signature;
    }

    public void setSignature(SignatureDTO signature) {
        this.signature = signature;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderConfirmationDTO)) {
            return false;
        }

        OrderConfirmationDTO orderConfirmationDTO = (OrderConfirmationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, orderConfirmationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderConfirmationDTO{" +
            "id=" + getId() +
            ", remoteId=" + getRemoteId() +
            ", number='" + getNumber() + "'" +
            ", contactName='" + getContactName() + "'" +
            ", date='" + getDate() + "'" +
            ", periodText='" + getPeriodText() + "'" +
            ", currency='" + getCurrency() + "'" +
            ", total=" + getTotal() +
            ", vatIncluded='" + getVatIncluded() + "'" +
            ", discountRate=" + getDiscountRate() +
            ", discountType='" + getDiscountType() + "'" +
            ", language='" + getLanguage() + "'" +
            ", pageAmount=" + getPageAmount() +
            ", notes='" + getNotes() + "'" +
            ", status='" + getStatus() + "'" +
            ", created='" + getCreated() + "'" +
            ", contact=" + getContact() +
            ", contactAddress=" + getContactAddress() +
            ", contactPerson=" + getContactPerson() +
            ", contactPrePageAddress=" + getContactPrePageAddress() +
            ", layout=" + getLayout() +
            ", signature=" + getSignature() +
            "}";
    }
}