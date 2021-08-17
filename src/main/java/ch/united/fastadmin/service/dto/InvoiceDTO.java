package ch.united.fastadmin.service.dto;

import ch.united.fastadmin.domain.enumeration.Currency;
import ch.united.fastadmin.domain.enumeration.DiscountType;
import ch.united.fastadmin.domain.enumeration.DocumentLanguage;
import ch.united.fastadmin.domain.enumeration.InvoiceStatus;
import ch.united.fastadmin.domain.enumeration.IsrPosition;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ch.united.fastadmin.domain.Invoice} entity.
 */
public class InvoiceDTO implements Serializable {

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
     * due date of the invoice
     */
    @ApiModelProperty(value = "due date of the invoice")
    private LocalDate due;

    /**
     * service period from
     */
    @ApiModelProperty(value = "service period from")
    private LocalDate periodFrom;

    /**
     * service period to
     */
    @ApiModelProperty(value = "service period to")
    private LocalDate periodTo;

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
     * cash discount value in %
     */
    @ApiModelProperty(value = "cash discount value in %")
    private Integer cashDiscountRate;

    /**
     * cash discount deadline date
     */
    @ApiModelProperty(value = "cash discount deadline date")
    private LocalDate cashDiscountDate;

    /**
     * paid amount of the invoice
     */
    @ApiModelProperty(value = "paid amount of the invoice")
    private Double totalPaid;

    /**
     * date when invoice was fully paid
     */
    @ApiModelProperty(value = "date when invoice was fully paid")
    private String paidDate;

    /**
     * ISR position; possible values: A - additional page, F - first page, L - last page
     */
    @ApiModelProperty(value = "ISR position; possible values: A - additional page, F - first page, L - last page")
    private IsrPosition isrPosition;

    /**
     * ISR reference number
     */
    @ApiModelProperty(value = "ISR reference number")
    private String isrReferenceNumber;

    /**
     * whether to print PayPal link on invoice
     */
    @ApiModelProperty(value = "whether to print PayPal link on invoice")
    private Boolean paymentLinkPaypal;

    /**
     * PayPal link URL
     */
    @ApiModelProperty(value = "PayPal link URL")
    private String paymentLinkPaypalUrl;

    /**
     * whether to print PostFinance link on invoice
     */
    @ApiModelProperty(value = "whether to print PostFinance link on invoice")
    private Boolean paymentLinkPostfinance;

    /**
     * PostFinance link URL
     */
    @ApiModelProperty(value = "PostFinance link URL")
    private String paymentLinkPostfinanceUrl;

    /**
     * whether to print Payrexx link on invoice
     */
    @ApiModelProperty(value = "whether to print Payrexx link on invoice")
    private Boolean paymentLinkPayrexx;

    /**
     * Payrexx link URL
     */
    @ApiModelProperty(value = "Payrexx link URL")
    private String paymentLinkPayrexxUrl;

    /**
     * whether to print SmartCommerce link on invoice
     */
    @ApiModelProperty(value = "whether to print SmartCommerce link on invoice")
    private Boolean paymentLinkSmartcommerce;

    /**
     * SmartCommerce link URL
     */
    @ApiModelProperty(value = "SmartCommerce link URL")
    private String paymentLinkSmartcommerceUrl;

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
     * status of invoice, possible values: DR - draft, S - sent, P - paid,\nPP - partially paid, R1 - 1st reminder, R2 - 2nd reminder, R3 - 3rd reminder,\nR - reminder, DC - debt collection, C - cancelled, D - deleted (but still visible)
     */
    @ApiModelProperty(
        value = "status of invoice, possible values: DR - draft, S - sent, P - paid,\nPP - partially paid, R1 - 1st reminder, R2 - 2nd reminder, R3 - 3rd reminder,\nR - reminder, DC - debt collection, C - cancelled, D - deleted (but still visible)"
    )
    private InvoiceStatus status;

    private ZonedDateTime created;

    private ContactDTO contact;

    private ContactAddressDTO contactAddress;

    private ContactPersonDTO contactPerson;

    private ContactAddressDTO contactPrePageAddress;

    private LayoutDTO layout;

    private SignatureDTO signature;

    private BankAccountDTO bankAccount;

    private IsrDTO isr;

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

    public LocalDate getDue() {
        return due;
    }

    public void setDue(LocalDate due) {
        this.due = due;
    }

    public LocalDate getPeriodFrom() {
        return periodFrom;
    }

    public void setPeriodFrom(LocalDate periodFrom) {
        this.periodFrom = periodFrom;
    }

    public LocalDate getPeriodTo() {
        return periodTo;
    }

    public void setPeriodTo(LocalDate periodTo) {
        this.periodTo = periodTo;
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

    public Integer getCashDiscountRate() {
        return cashDiscountRate;
    }

    public void setCashDiscountRate(Integer cashDiscountRate) {
        this.cashDiscountRate = cashDiscountRate;
    }

    public LocalDate getCashDiscountDate() {
        return cashDiscountDate;
    }

    public void setCashDiscountDate(LocalDate cashDiscountDate) {
        this.cashDiscountDate = cashDiscountDate;
    }

    public Double getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(Double totalPaid) {
        this.totalPaid = totalPaid;
    }

    public String getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(String paidDate) {
        this.paidDate = paidDate;
    }

    public IsrPosition getIsrPosition() {
        return isrPosition;
    }

    public void setIsrPosition(IsrPosition isrPosition) {
        this.isrPosition = isrPosition;
    }

    public String getIsrReferenceNumber() {
        return isrReferenceNumber;
    }

    public void setIsrReferenceNumber(String isrReferenceNumber) {
        this.isrReferenceNumber = isrReferenceNumber;
    }

    public Boolean getPaymentLinkPaypal() {
        return paymentLinkPaypal;
    }

    public void setPaymentLinkPaypal(Boolean paymentLinkPaypal) {
        this.paymentLinkPaypal = paymentLinkPaypal;
    }

    public String getPaymentLinkPaypalUrl() {
        return paymentLinkPaypalUrl;
    }

    public void setPaymentLinkPaypalUrl(String paymentLinkPaypalUrl) {
        this.paymentLinkPaypalUrl = paymentLinkPaypalUrl;
    }

    public Boolean getPaymentLinkPostfinance() {
        return paymentLinkPostfinance;
    }

    public void setPaymentLinkPostfinance(Boolean paymentLinkPostfinance) {
        this.paymentLinkPostfinance = paymentLinkPostfinance;
    }

    public String getPaymentLinkPostfinanceUrl() {
        return paymentLinkPostfinanceUrl;
    }

    public void setPaymentLinkPostfinanceUrl(String paymentLinkPostfinanceUrl) {
        this.paymentLinkPostfinanceUrl = paymentLinkPostfinanceUrl;
    }

    public Boolean getPaymentLinkPayrexx() {
        return paymentLinkPayrexx;
    }

    public void setPaymentLinkPayrexx(Boolean paymentLinkPayrexx) {
        this.paymentLinkPayrexx = paymentLinkPayrexx;
    }

    public String getPaymentLinkPayrexxUrl() {
        return paymentLinkPayrexxUrl;
    }

    public void setPaymentLinkPayrexxUrl(String paymentLinkPayrexxUrl) {
        this.paymentLinkPayrexxUrl = paymentLinkPayrexxUrl;
    }

    public Boolean getPaymentLinkSmartcommerce() {
        return paymentLinkSmartcommerce;
    }

    public void setPaymentLinkSmartcommerce(Boolean paymentLinkSmartcommerce) {
        this.paymentLinkSmartcommerce = paymentLinkSmartcommerce;
    }

    public String getPaymentLinkSmartcommerceUrl() {
        return paymentLinkSmartcommerceUrl;
    }

    public void setPaymentLinkSmartcommerceUrl(String paymentLinkSmartcommerceUrl) {
        this.paymentLinkSmartcommerceUrl = paymentLinkSmartcommerceUrl;
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

    public InvoiceStatus getStatus() {
        return status;
    }

    public void setStatus(InvoiceStatus status) {
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

    public BankAccountDTO getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(BankAccountDTO bankAccount) {
        this.bankAccount = bankAccount;
    }

    public IsrDTO getIsr() {
        return isr;
    }

    public void setIsr(IsrDTO isr) {
        this.isr = isr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvoiceDTO)) {
            return false;
        }

        InvoiceDTO invoiceDTO = (InvoiceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, invoiceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvoiceDTO{" +
            "id=" + getId() +
            ", remoteId=" + getRemoteId() +
            ", number='" + getNumber() + "'" +
            ", contactName='" + getContactName() + "'" +
            ", date='" + getDate() + "'" +
            ", due='" + getDue() + "'" +
            ", periodFrom='" + getPeriodFrom() + "'" +
            ", periodTo='" + getPeriodTo() + "'" +
            ", periodText='" + getPeriodText() + "'" +
            ", currency='" + getCurrency() + "'" +
            ", total=" + getTotal() +
            ", vatIncluded='" + getVatIncluded() + "'" +
            ", discountRate=" + getDiscountRate() +
            ", discountType='" + getDiscountType() + "'" +
            ", cashDiscountRate=" + getCashDiscountRate() +
            ", cashDiscountDate='" + getCashDiscountDate() + "'" +
            ", totalPaid=" + getTotalPaid() +
            ", paidDate='" + getPaidDate() + "'" +
            ", isrPosition='" + getIsrPosition() + "'" +
            ", isrReferenceNumber='" + getIsrReferenceNumber() + "'" +
            ", paymentLinkPaypal='" + getPaymentLinkPaypal() + "'" +
            ", paymentLinkPaypalUrl='" + getPaymentLinkPaypalUrl() + "'" +
            ", paymentLinkPostfinance='" + getPaymentLinkPostfinance() + "'" +
            ", paymentLinkPostfinanceUrl='" + getPaymentLinkPostfinanceUrl() + "'" +
            ", paymentLinkPayrexx='" + getPaymentLinkPayrexx() + "'" +
            ", paymentLinkPayrexxUrl='" + getPaymentLinkPayrexxUrl() + "'" +
            ", paymentLinkSmartcommerce='" + getPaymentLinkSmartcommerce() + "'" +
            ", paymentLinkSmartcommerceUrl='" + getPaymentLinkSmartcommerceUrl() + "'" +
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
            ", bankAccount=" + getBankAccount() +
            ", isr=" + getIsr() +
            "}";
    }
}
