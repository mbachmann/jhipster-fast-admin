package ch.united.fastadmin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * free texts objects
 */
@Entity
@Table(name = "document_free_text")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DocumentFreeText implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * displayed text
     */
    @Column(name = "text")
    private String text;

    /**
     * font size of displayed text (min: 1; max: 50)
     */
    @Column(name = "font_size")
    private Integer fontSize;

    /**
     * The x position of the free text
     */
    @Column(name = "position_x")
    private Double positionX;

    /**
     * The y position of the free text
     */
    @Column(name = "position_y")
    private Double positionY;

    /**
     * which page text is displayed on
     */
    @Column(name = "page_no")
    private Integer pageNo;

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "customFields", "freeTexts", "contact", "contactAddress", "contactPerson", "contactPrePageAddress", "layout", "layout" },
        allowSetters = true
    )
    private DocumentLetter documentLetter;

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

    public DocumentFreeText id(Long id) {
        this.id = id;
        return this;
    }

    public String getText() {
        return this.text;
    }

    public DocumentFreeText text(String text) {
        this.text = text;
        return this;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getFontSize() {
        return this.fontSize;
    }

    public DocumentFreeText fontSize(Integer fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    public Double getPositionX() {
        return this.positionX;
    }

    public DocumentFreeText positionX(Double positionX) {
        this.positionX = positionX;
        return this;
    }

    public void setPositionX(Double positionX) {
        this.positionX = positionX;
    }

    public Double getPositionY() {
        return this.positionY;
    }

    public DocumentFreeText positionY(Double positionY) {
        this.positionY = positionY;
        return this;
    }

    public void setPositionY(Double positionY) {
        this.positionY = positionY;
    }

    public Integer getPageNo() {
        return this.pageNo;
    }

    public DocumentFreeText pageNo(Integer pageNo) {
        this.pageNo = pageNo;
        return this;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public DocumentLetter getDocumentLetter() {
        return this.documentLetter;
    }

    public DocumentFreeText documentLetter(DocumentLetter documentLetter) {
        this.setDocumentLetter(documentLetter);
        return this;
    }

    public void setDocumentLetter(DocumentLetter documentLetter) {
        this.documentLetter = documentLetter;
    }

    public DeliveryNote getDeliveryNote() {
        return this.deliveryNote;
    }

    public DocumentFreeText deliveryNote(DeliveryNote deliveryNote) {
        this.setDeliveryNote(deliveryNote);
        return this;
    }

    public void setDeliveryNote(DeliveryNote deliveryNote) {
        this.deliveryNote = deliveryNote;
    }

    public Invoice getInvoice() {
        return this.invoice;
    }

    public DocumentFreeText invoice(Invoice invoice) {
        this.setInvoice(invoice);
        return this;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Offer getOffer() {
        return this.offer;
    }

    public DocumentFreeText offer(Offer offer) {
        this.setOffer(offer);
        return this;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public OrderConfirmation getOrderConfirmation() {
        return this.orderConfirmation;
    }

    public DocumentFreeText orderConfirmation(OrderConfirmation orderConfirmation) {
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
        if (!(o instanceof DocumentFreeText)) {
            return false;
        }
        return id != null && id.equals(((DocumentFreeText) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentFreeText{" +
            "id=" + getId() +
            ", text='" + getText() + "'" +
            ", fontSize=" + getFontSize() +
            ", positionX=" + getPositionX() +
            ", positionY=" + getPositionY() +
            ", pageNo=" + getPageNo() +
            "}";
    }
}
