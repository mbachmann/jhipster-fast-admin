package ch.united.fastadmin.domain;

import ch.united.fastadmin.domain.enumeration.CatalogScope;
import ch.united.fastadmin.domain.enumeration.DiscountType;
import ch.united.fastadmin.domain.enumeration.DocumentPositionType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * the document position
 */
@Entity
@Table(name = "document_position")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DocumentPosition implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * position type; possible values: N - normal, T - text, PB - page break, H - header, SI - subtotal (incremental), SNI - subtotal (non incremental)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private DocumentPositionType type;

    /**
     * position catalog type; possible values: P - product, S - service
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "catalog_type")
    private CatalogScope catalogType;

    /**
     * catalog unique number
     */
    @Column(name = "number")
    private String number;

    /**
     * position name
     */
    @Column(name = "name")
    private String name;

    /**
     * position description
     */
    @Column(name = "description")
    private String description;

    /**
     * price per unit; required for N type
     */
    @Column(name = "price")
    private Double price;

    /**
     * VAT rate; required for N type
     */
    @Column(name = "vat")
    private Double vat;

    /**
     * amount; required for N type
     */
    @Column(name = "amount")
    private Double amount;

    /**
     * optional discount rate
     */
    @Column(name = "discount_rate")
    private Double discountRate;

    /**
     * optional discount type; possible values:A - amount, P - percentage
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "discount_type")
    private DiscountType discountType;

    /**
     * total price
     */
    @Column(name = "total")
    private Double total;

    /**
     * whether to show only total on invoice
     */
    @Column(name = "show_only_total")
    private Boolean showOnlyTotal;

    /**
     * the related catalog unit
     */
    @ManyToOne
    private CatalogUnit unit;

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

    public DocumentPosition id(Long id) {
        this.id = id;
        return this;
    }

    public DocumentPositionType getType() {
        return this.type;
    }

    public DocumentPosition type(DocumentPositionType type) {
        this.type = type;
        return this;
    }

    public void setType(DocumentPositionType type) {
        this.type = type;
    }

    public CatalogScope getCatalogType() {
        return this.catalogType;
    }

    public DocumentPosition catalogType(CatalogScope catalogType) {
        this.catalogType = catalogType;
        return this;
    }

    public void setCatalogType(CatalogScope catalogType) {
        this.catalogType = catalogType;
    }

    public String getNumber() {
        return this.number;
    }

    public DocumentPosition number(String number) {
        this.number = number;
        return this;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return this.name;
    }

    public DocumentPosition name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public DocumentPosition description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return this.price;
    }

    public DocumentPosition price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getVat() {
        return this.vat;
    }

    public DocumentPosition vat(Double vat) {
        this.vat = vat;
        return this;
    }

    public void setVat(Double vat) {
        this.vat = vat;
    }

    public Double getAmount() {
        return this.amount;
    }

    public DocumentPosition amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getDiscountRate() {
        return this.discountRate;
    }

    public DocumentPosition discountRate(Double discountRate) {
        this.discountRate = discountRate;
        return this;
    }

    public void setDiscountRate(Double discountRate) {
        this.discountRate = discountRate;
    }

    public DiscountType getDiscountType() {
        return this.discountType;
    }

    public DocumentPosition discountType(DiscountType discountType) {
        this.discountType = discountType;
        return this;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public Double getTotal() {
        return this.total;
    }

    public DocumentPosition total(Double total) {
        this.total = total;
        return this;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Boolean getShowOnlyTotal() {
        return this.showOnlyTotal;
    }

    public DocumentPosition showOnlyTotal(Boolean showOnlyTotal) {
        this.showOnlyTotal = showOnlyTotal;
        return this;
    }

    public void setShowOnlyTotal(Boolean showOnlyTotal) {
        this.showOnlyTotal = showOnlyTotal;
    }

    public CatalogUnit getUnit() {
        return this.unit;
    }

    public DocumentPosition unit(CatalogUnit catalogUnit) {
        this.setUnit(catalogUnit);
        return this;
    }

    public void setUnit(CatalogUnit catalogUnit) {
        this.unit = catalogUnit;
    }

    public DeliveryNote getDeliveryNote() {
        return this.deliveryNote;
    }

    public DocumentPosition deliveryNote(DeliveryNote deliveryNote) {
        this.setDeliveryNote(deliveryNote);
        return this;
    }

    public void setDeliveryNote(DeliveryNote deliveryNote) {
        this.deliveryNote = deliveryNote;
    }

    public Invoice getInvoice() {
        return this.invoice;
    }

    public DocumentPosition invoice(Invoice invoice) {
        this.setInvoice(invoice);
        return this;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Offer getOffer() {
        return this.offer;
    }

    public DocumentPosition offer(Offer offer) {
        this.setOffer(offer);
        return this;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public OrderConfirmation getOrderConfirmation() {
        return this.orderConfirmation;
    }

    public DocumentPosition orderConfirmation(OrderConfirmation orderConfirmation) {
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
        if (!(o instanceof DocumentPosition)) {
            return false;
        }
        return id != null && id.equals(((DocumentPosition) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentPosition{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", catalogType='" + getCatalogType() + "'" +
            ", number='" + getNumber() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", price=" + getPrice() +
            ", vat=" + getVat() +
            ", amount=" + getAmount() +
            ", discountRate=" + getDiscountRate() +
            ", discountType='" + getDiscountType() + "'" +
            ", total=" + getTotal() +
            ", showOnlyTotal='" + getShowOnlyTotal() + "'" +
            "}";
    }
}
