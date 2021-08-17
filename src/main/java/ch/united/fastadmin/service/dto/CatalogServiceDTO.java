package ch.united.fastadmin.service.dto;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ch.united.fastadmin.domain.CatalogService} entity.
 */
public class CatalogServiceDTO implements Serializable {

    private Long id;

    /**
     * id of a remote system
     */

    @ApiModelProperty(value = "id of a remote system")
    private Integer remoteId;

    /**
     * Catalog Product Number
     */

    @ApiModelProperty(value = "Catalog Product Number")
    private String number;

    /**
     * Catalog Product Name
     */
    @NotNull
    @ApiModelProperty(value = "Catalog Product Name", required = true)
    private String name;

    /**
     * Catalog Product Description
     */
    @ApiModelProperty(value = "Catalog Product Description")
    private String description;

    /**
     * Catalog Product Notes
     */
    @ApiModelProperty(value = "Catalog Product Notes")
    private String notes;

    /**
     * Category Name
     */
    @ApiModelProperty(value = "Category Name")
    private String categoryName;

    /**
     * The price includes MWST
     */
    @ApiModelProperty(value = "The price includes MWST")
    private Boolean includingVat;

    /**
     * The VAT Percent
     */
    @ApiModelProperty(value = "The VAT Percent")
    private Double vat;

    /**
     * The unit Name
     */
    @ApiModelProperty(value = "The unit Name")
    private String unitName;

    /**
     * The sales price of the product
     */
    @ApiModelProperty(value = "The sales price of the product")
    private Double price;

    /**
     * Default amount of products (Standard Menge)
     */
    @ApiModelProperty(value = "Default amount of products (Standard Menge)")
    private Integer defaultAmount;

    private ZonedDateTime created;

    /**
     * is not active anymore
     */
    @ApiModelProperty(value = "is not active anymore")
    private Boolean inactiv;

    private CatalogCategoryDTO category;

    private CatalogUnitDTO unit;

    private ValueAddedTaxDTO valueAddedTax;

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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Boolean getIncludingVat() {
        return includingVat;
    }

    public void setIncludingVat(Boolean includingVat) {
        this.includingVat = includingVat;
    }

    public Double getVat() {
        return vat;
    }

    public void setVat(Double vat) {
        this.vat = vat;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getDefaultAmount() {
        return defaultAmount;
    }

    public void setDefaultAmount(Integer defaultAmount) {
        this.defaultAmount = defaultAmount;
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

    public CatalogCategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CatalogCategoryDTO category) {
        this.category = category;
    }

    public CatalogUnitDTO getUnit() {
        return unit;
    }

    public void setUnit(CatalogUnitDTO unit) {
        this.unit = unit;
    }

    public ValueAddedTaxDTO getValueAddedTax() {
        return valueAddedTax;
    }

    public void setValueAddedTax(ValueAddedTaxDTO valueAddedTax) {
        this.valueAddedTax = valueAddedTax;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CatalogServiceDTO)) {
            return false;
        }

        CatalogServiceDTO catalogServiceDTO = (CatalogServiceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, catalogServiceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CatalogServiceDTO{" +
            "id=" + getId() +
            ", remoteId=" + getRemoteId() +
            ", number='" + getNumber() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", notes='" + getNotes() + "'" +
            ", categoryName='" + getCategoryName() + "'" +
            ", includingVat='" + getIncludingVat() + "'" +
            ", vat=" + getVat() +
            ", unitName='" + getUnitName() + "'" +
            ", price=" + getPrice() +
            ", defaultAmount=" + getDefaultAmount() +
            ", created='" + getCreated() + "'" +
            ", inactiv='" + getInactiv() + "'" +
            ", category=" + getCategory() +
            ", unit=" + getUnit() +
            ", valueAddedTax=" + getValueAddedTax() +
            "}";
    }
}
