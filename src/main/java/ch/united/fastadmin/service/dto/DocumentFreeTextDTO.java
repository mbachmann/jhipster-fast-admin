package ch.united.fastadmin.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ch.united.fastadmin.domain.DocumentFreeText} entity.
 */
@ApiModel(description = "free texts objects")
public class DocumentFreeTextDTO implements Serializable {

    private Long id;

    /**
     * displayed text
     */
    @ApiModelProperty(value = "displayed text")
    private String text;

    /**
     * font size of displayed text (min: 1; max: 50)
     */
    @ApiModelProperty(value = "font size of displayed text (min: 1; max: 50)")
    private Integer fontSize;

    /**
     * The x position of the free text
     */
    @ApiModelProperty(value = "The x position of the free text")
    private Double positionX;

    /**
     * The y position of the free text
     */
    @ApiModelProperty(value = "The y position of the free text")
    private Double positionY;

    /**
     * which page text is displayed on
     */
    @ApiModelProperty(value = "which page text is displayed on")
    private Integer pageNo;

    private DocumentLetterDTO documentLetter;

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getFontSize() {
        return fontSize;
    }

    public void setFontSize(Integer fontSize) {
        this.fontSize = fontSize;
    }

    public Double getPositionX() {
        return positionX;
    }

    public void setPositionX(Double positionX) {
        this.positionX = positionX;
    }

    public Double getPositionY() {
        return positionY;
    }

    public void setPositionY(Double positionY) {
        this.positionY = positionY;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public DocumentLetterDTO getDocumentLetter() {
        return documentLetter;
    }

    public void setDocumentLetter(DocumentLetterDTO documentLetter) {
        this.documentLetter = documentLetter;
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
        if (!(o instanceof DocumentFreeTextDTO)) {
            return false;
        }

        DocumentFreeTextDTO documentFreeTextDTO = (DocumentFreeTextDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, documentFreeTextDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentFreeTextDTO{" +
            "id=" + getId() +
            ", text='" + getText() + "'" +
            ", fontSize=" + getFontSize() +
            ", positionX=" + getPositionX() +
            ", positionY=" + getPositionY() +
            ", pageNo=" + getPageNo() +
            ", documentLetter=" + getDocumentLetter() +
            ", deliveryNote=" + getDeliveryNote() +
            ", invoice=" + getInvoice() +
            ", offer=" + getOffer() +
            ", orderConfirmation=" + getOrderConfirmation() +
            "}";
    }
}
