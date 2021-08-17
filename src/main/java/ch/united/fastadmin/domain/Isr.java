package ch.united.fastadmin.domain;

import ch.united.fastadmin.domain.enumeration.IsrPosition;
import ch.united.fastadmin.domain.enumeration.IsrType;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * ISR definition for ISR - orange inpayment slip, ISR+ - orange inpayment slip plus, RIS - red inpayment slip, QR Code and QR Code with reference
 */
@Entity
@Table(name = "isr")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Isr implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * whether ISR is a default one
     */
    @Column(name = "default_isr")
    private Boolean defaultIsr;

    /**
     * SR type; possible values: ISR - orange inpayment slip, ISR+ - orange inpayment slip plus, RIS - red inpayment slip, QR Code and QR Code with reference
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private IsrType type;

    /**
     * ISR position; possible values: A - additional page, F - first page, L - last page
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "position")
    private IsrPosition position;

    /**
     * optional ISR name (for internal system use only)
     */
    @Column(name = "name")
    private String name;

    /**
     * Name of the bank
     */
    @Column(name = "bank_name")
    private String bankName;

    /**
     * Address of the bank
     */
    @Column(name = "bank_address")
    private String bankAddress;

    /**
     * name of the recipient
     */
    @Column(name = "recipient_name")
    private String recipientName;

    /**
     * additional information of the recipient
     */
    @Column(name = "recipient_addition")
    private String recipientAddition;

    /**
     * street of the recipient
     */
    @Column(name = "recipient_street")
    private String recipientStreet;

    /**
     * city of the recipient
     */
    @Column(name = "recipient_city")
    private String recipientCity;

    /**
     * the number (BESR or REF)
     */
    @Column(name = "delivery_number")
    private String deliveryNumber;

    /**
     * the IBAN account number
     */
    @Column(name = "iban")
    private String iban;

    /**
     * the subscriber number (Teilnehmernummer)
     */
    @Column(name = "subscriber_number")
    private String subscriberNumber;

    /**
     * left print adjust in mm
     */
    @Column(name = "left_print_adjust")
    private Double leftPrintAdjust;

    /**
     * top print adjust in mm
     */
    @Column(name = "top_print_adjust")
    private Double topPrintAdjust;

    @Column(name = "created")
    private ZonedDateTime created;

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

    public Isr id(Long id) {
        this.id = id;
        return this;
    }

    public Boolean getDefaultIsr() {
        return this.defaultIsr;
    }

    public Isr defaultIsr(Boolean defaultIsr) {
        this.defaultIsr = defaultIsr;
        return this;
    }

    public void setDefaultIsr(Boolean defaultIsr) {
        this.defaultIsr = defaultIsr;
    }

    public IsrType getType() {
        return this.type;
    }

    public Isr type(IsrType type) {
        this.type = type;
        return this;
    }

    public void setType(IsrType type) {
        this.type = type;
    }

    public IsrPosition getPosition() {
        return this.position;
    }

    public Isr position(IsrPosition position) {
        this.position = position;
        return this;
    }

    public void setPosition(IsrPosition position) {
        this.position = position;
    }

    public String getName() {
        return this.name;
    }

    public Isr name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBankName() {
        return this.bankName;
    }

    public Isr bankName(String bankName) {
        this.bankName = bankName;
        return this;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAddress() {
        return this.bankAddress;
    }

    public Isr bankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
        return this;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getRecipientName() {
        return this.recipientName;
    }

    public Isr recipientName(String recipientName) {
        this.recipientName = recipientName;
        return this;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientAddition() {
        return this.recipientAddition;
    }

    public Isr recipientAddition(String recipientAddition) {
        this.recipientAddition = recipientAddition;
        return this;
    }

    public void setRecipientAddition(String recipientAddition) {
        this.recipientAddition = recipientAddition;
    }

    public String getRecipientStreet() {
        return this.recipientStreet;
    }

    public Isr recipientStreet(String recipientStreet) {
        this.recipientStreet = recipientStreet;
        return this;
    }

    public void setRecipientStreet(String recipientStreet) {
        this.recipientStreet = recipientStreet;
    }

    public String getRecipientCity() {
        return this.recipientCity;
    }

    public Isr recipientCity(String recipientCity) {
        this.recipientCity = recipientCity;
        return this;
    }

    public void setRecipientCity(String recipientCity) {
        this.recipientCity = recipientCity;
    }

    public String getDeliveryNumber() {
        return this.deliveryNumber;
    }

    public Isr deliveryNumber(String deliveryNumber) {
        this.deliveryNumber = deliveryNumber;
        return this;
    }

    public void setDeliveryNumber(String deliveryNumber) {
        this.deliveryNumber = deliveryNumber;
    }

    public String getIban() {
        return this.iban;
    }

    public Isr iban(String iban) {
        this.iban = iban;
        return this;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getSubscriberNumber() {
        return this.subscriberNumber;
    }

    public Isr subscriberNumber(String subscriberNumber) {
        this.subscriberNumber = subscriberNumber;
        return this;
    }

    public void setSubscriberNumber(String subscriberNumber) {
        this.subscriberNumber = subscriberNumber;
    }

    public Double getLeftPrintAdjust() {
        return this.leftPrintAdjust;
    }

    public Isr leftPrintAdjust(Double leftPrintAdjust) {
        this.leftPrintAdjust = leftPrintAdjust;
        return this;
    }

    public void setLeftPrintAdjust(Double leftPrintAdjust) {
        this.leftPrintAdjust = leftPrintAdjust;
    }

    public Double getTopPrintAdjust() {
        return this.topPrintAdjust;
    }

    public Isr topPrintAdjust(Double topPrintAdjust) {
        this.topPrintAdjust = topPrintAdjust;
        return this;
    }

    public void setTopPrintAdjust(Double topPrintAdjust) {
        this.topPrintAdjust = topPrintAdjust;
    }

    public ZonedDateTime getCreated() {
        return this.created;
    }

    public Isr created(ZonedDateTime created) {
        this.created = created;
        return this;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public Boolean getInactiv() {
        return this.inactiv;
    }

    public Isr inactiv(Boolean inactiv) {
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
        if (!(o instanceof Isr)) {
            return false;
        }
        return id != null && id.equals(((Isr) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Isr{" +
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
