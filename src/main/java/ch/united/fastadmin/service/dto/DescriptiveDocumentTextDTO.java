package ch.united.fastadmin.service.dto;

import ch.united.fastadmin.domain.enumeration.DocumentInvoiceTextStatus;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ch.united.fastadmin.domain.DescriptiveDocumentText} entity.
 */
public class DescriptiveDocumentTextDTO implements Serializable {

    private Long id;

    /**
     * Document title text
     */
    @ApiModelProperty(value = "Document title text")
    private String title;

    /**
     * Document introduction text
     */
    @ApiModelProperty(value = "Document introduction text")
    private String introduction;

    /**
     * Document conditions text (z.B. Zahlungsbedinungen)
     */
    @ApiModelProperty(value = "Document conditions text (z.B. Zahlungsbedinungen)")
    private String conditions;

    /**
     * for invoice only
     */
    @ApiModelProperty(value = "for invoice only")
    private DocumentInvoiceTextStatus status;

    private DeliveryNoteDTO deliveryNote;

    private InvoiceDTO invoice;

    private OfferDTO offer;

    private OrderConfirmationDTO orderConfirmation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public DocumentInvoiceTextStatus getStatus() {
        return status;
    }

    public void setStatus(DocumentInvoiceTextStatus status) {
        this.status = status;
    }

    public DeliveryNoteDTO getDeliveryNote() {
        return deliveryNote;
    }

    public void setDeliveryNote(DeliveryNoteDTO deliveryNote) {
        this.deliveryNote = deliveryNote;
    }

    public InvoiceDTO getInvoice() {
        return invoice;
    }

    public void setInvoice(InvoiceDTO invoice) {
        this.invoice = invoice;
    }

    public OfferDTO getOffer() {
        return offer;
    }

    public void setOffer(OfferDTO offer) {
        this.offer = offer;
    }

    public OrderConfirmationDTO getOrderConfirmation() {
        return orderConfirmation;
    }

    public void setOrderConfirmation(OrderConfirmationDTO orderConfirmation) {
        this.orderConfirmation = orderConfirmation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DescriptiveDocumentTextDTO)) {
            return false;
        }

        DescriptiveDocumentTextDTO descriptiveDocumentTextDTO = (DescriptiveDocumentTextDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, descriptiveDocumentTextDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DescriptiveDocumentTextDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", introduction='" + getIntroduction() + "'" +
            ", conditions='" + getConditions() + "'" +
            ", status='" + getStatus() + "'" +
            ", deliveryNote=" + getDeliveryNote() +
            ", invoice=" + getInvoice() +
            ", offer=" + getOffer() +
            ", orderConfirmation=" + getOrderConfirmation() +
            "}";
    }
}
