package ch.united.fastadmin.service.dto;

import ch.united.fastadmin.domain.enumeration.CatalogScope;
import ch.united.fastadmin.domain.enumeration.DiscountType;
import ch.united.fastadmin.domain.enumeration.DocumentPositionType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link ch.united.fastadmin.domain.DocumentPosition} entity.
 */
@ApiModel(description = "the document position")
public class DocumentPositionDTO implements Serializable {

    private Long id;

    /**
     * position type; possible values: N - normal, T - text, PB - page break, H - header, SI - subtotal (incremental), SNI - subtotal (non incremental)
     */
    @ApiModelProperty(
        value = "position type; possible values: N - normal, T - text, PB - page break, H - header, SI - subtotal (incremental), SNI - subtotal (non incremental)"
    )
    private DocumentPositionType type;

    /**
     * position catalog type; possible values: P - product, S - service
     */
    @ApiModelProperty(value = "position catalog type; possible values: P - product, S - service")
    private CatalogScope catalogType;

    /**
     * catalog unique number
     */
    @ApiModelProperty(value = "catalog unique number")
    private String number;

    /**
     * position name
     */
    @ApiModelProperty(value = "position name")
    private String name;

    /**
     * position description
     */
    @ApiModelProperty(value = "position description")
    private String description;

    /**
     * price per unit; required for N type
     */
    @ApiModelProperty(value = "price per unit; required for N type")
    private Double price;

    /**
     * VAT rate; required for N type
     */
    @ApiModelProperty(value = "VAT rate; required for N type")
    private Double vat;

    /**
     * amount; required for N type
     */
    @ApiModelProperty(value = "amount; required for N type")
    private Double amount;

    /**
     * optional discount rate
     */
    @ApiModelProperty(value = "optional discount rate")
    private Double discountRate;

    /**
     * optional discount type; possible values:A - amount, P - percentage
     */
    @ApiModelProperty(value = "optional discount type; possible values:A - amount, P - percentage")
    private DiscountType discountType;

    /**
     * total price
     */
    @ApiModelProperty(value = "total price")
    private Double total;

    /**
     * whether to show only total on invoice
     */
    @ApiModelProperty(value = "whether to show only total on invoice")
    private Boolean showOnlyTotal;

    private CatalogUnitDTO unit;

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

    public DocumentPositionType getType() {
        return type;
    }

    public void setType(DocumentPositionType type) {
        this.type = type;
    }

    public CatalogScope getCatalogType() {
        return catalogType;
    }

    public void setCatalogType(CatalogScope catalogType) {
        this.catalogType = catalogType;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getVat() {
        return vat;
    }

    public void setVat(Double vat) {
        this.vat = vat;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
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

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Boolean getShowOnlyTotal() {
        return showOnlyTotal;
    }

    public void setShowOnlyTotal(Boolean showOnlyTotal) {
        this.showOnlyTotal = showOnlyTotal;
    }

    public CatalogUnitDTO getUnit() {
        return unit;
    }

    public void setUnit(CatalogUnitDTO unit) {
        this.unit = unit;
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
        if (!(o instanceof DocumentPositionDTO)) {
            return false;
        }

        DocumentPositionDTO documentPositionDTO = (DocumentPositionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, documentPositionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentPositionDTO{" +
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
            ", unit=" + getUnit() +
            ", deliveryNote=" + getDeliveryNote() +
            ", invoice=" + getInvoice() +
            ", offer=" + getOffer() +
            ", orderConfirmation=" + getOrderConfirmation() +
            "}";
    }
}
