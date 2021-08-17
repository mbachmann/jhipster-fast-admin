package ch.united.fastadmin.domain;

import ch.united.fastadmin.domain.enumeration.DocumentInvoiceTextStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DescriptiveDocumentText.
 */
@Entity
@Table(name = "descriptive_document_text")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DescriptiveDocumentText implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Document title text
     */
    @Column(name = "title")
    private String title;

    /**
     * Document introduction text
     */
    @Column(name = "introduction")
    private String introduction;

    /**
     * Document conditions text (z.B. Zahlungsbedinungen)
     */
    @Column(name = "conditions")
    private String conditions;

    /**
     * for invoice only
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DocumentInvoiceTextStatus status;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "customFields",
            "freeTexts",
            "texts",
            "positions",
            "contact",
            "contactAddress",
            "contactPerson",
            "contactPrePageAddress",
            "layout",
            "layout",
        },
        allowSetters = true
    )
    private DeliveryNote deliveryNote;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "freeTexts",
            "texts",
            "positions",
            "contact",
            "contactAddress",
            "contactPerson",
            "contactPrePageAddress",
            "layout",
            "layout",
            "bankAccount",
            "isr",
        },
        allowSetters = true
    )
    private Invoice invoice;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "freeTexts", "texts", "positions", "contact", "contactAddress", "contactPerson", "contactPrePageAddress", "layout", "layout",
        },
        allowSetters = true
    )
    private Offer offer;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "freeTexts", "texts", "positions", "contact", "contactAddress", "contactPerson", "contactPrePageAddress", "layout", "layout",
        },
        allowSetters = true
    )
    private OrderConfirmation orderConfirmation;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DescriptiveDocumentText id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public DescriptiveDocumentText title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroduction() {
        return this.introduction;
    }

    public DescriptiveDocumentText introduction(String introduction) {
        this.introduction = introduction;
        return this;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getConditions() {
        return this.conditions;
    }

    public DescriptiveDocumentText conditions(String conditions) {
        this.conditions = conditions;
        return this;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public DocumentInvoiceTextStatus getStatus() {
        return this.status;
    }

    public DescriptiveDocumentText status(DocumentInvoiceTextStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(DocumentInvoiceTextStatus status) {
        this.status = status;
    }

    public DeliveryNote getDeliveryNote() {
        return this.deliveryNote;
    }

    public DescriptiveDocumentText deliveryNote(DeliveryNote deliveryNote) {
        this.setDeliveryNote(deliveryNote);
        return this;
    }

    public void setDeliveryNote(DeliveryNote deliveryNote) {
        this.deliveryNote = deliveryNote;
    }

    public Invoice getInvoice() {
        return this.invoice;
    }

    public DescriptiveDocumentText invoice(Invoice invoice) {
        this.setInvoice(invoice);
        return this;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Offer getOffer() {
        return this.offer;
    }

    public DescriptiveDocumentText offer(Offer offer) {
        this.setOffer(offer);
        return this;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public OrderConfirmation getOrderConfirmation() {
        return this.orderConfirmation;
    }

    public DescriptiveDocumentText orderConfirmation(OrderConfirmation orderConfirmation) {
        this.setOrderConfirmation(orderConfirmation);
        return this;
    }

    public void setOrderConfirmation(OrderConfirmation orderConfirmation) {
        this.orderConfirmation = orderConfirmation;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DescriptiveDocumentText)) {
            return false;
        }
        return id != null && id.equals(((DescriptiveDocumentText) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DescriptiveDocumentText{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", introduction='" + getIntroduction() + "'" +
            ", conditions='" + getConditions() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
