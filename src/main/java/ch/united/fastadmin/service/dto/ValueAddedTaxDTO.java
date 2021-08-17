package ch.united.fastadmin.service.dto;

import ch.united.fastadmin.domain.enumeration.VatType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link ch.united.fastadmin.domain.ValueAddedTax} entity.
 */
@ApiModel(description = "Value added tax")
public class ValueAddedTaxDTO implements Serializable {

    private Long id;

    /**
     * Name of the VAT
     */
    @ApiModelProperty(value = "Name of the VAT")
    private String name;

    /**
     * VAT Type percent, no_vat, free_input
     */
    @ApiModelProperty(value = "VAT Type percent, no_vat, free_input")
    private VatType vatType;

    /**
     * VAT is valid from
     */
    @ApiModelProperty(value = "VAT is valid from")
    private LocalDate validFrom;

    /**
     * VAT is valid until
     */
    @ApiModelProperty(value = "VAT is valid until")
    private LocalDate validUntil;

    /**
     * The VAT percent
     */
    @ApiModelProperty(value = "The VAT percent")
    private Double vatPercent;

    /**
     * is not active anymore
     */
    @ApiModelProperty(value = "is not active anymore")
    private Boolean inactiv;

    /**
     * replaced by
     */
    @ApiModelProperty(value = "replaced by")
    private Integer newVatId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public VatType getVatType() {
        return vatType;
    }

    public void setVatType(VatType vatType) {
        this.vatType = vatType;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDate getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDate validUntil) {
        this.validUntil = validUntil;
    }

    public Double getVatPercent() {
        return vatPercent;
    }

    public void setVatPercent(Double vatPercent) {
        this.vatPercent = vatPercent;
    }

    public Boolean getInactiv() {
        return inactiv;
    }

    public void setInactiv(Boolean inactiv) {
        this.inactiv = inactiv;
    }

    public Integer getNewVatId() {
        return newVatId;
    }

    public void setNewVatId(Integer newVatId) {
        this.newVatId = newVatId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ValueAddedTaxDTO)) {
            return false;
        }

        ValueAddedTaxDTO valueAddedTaxDTO = (ValueAddedTaxDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, valueAddedTaxDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ValueAddedTaxDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", vatType='" + getVatType() + "'" +
            ", validFrom='" + getValidFrom() + "'" +
            ", validUntil='" + getValidUntil() + "'" +
            ", vatPercent=" + getVatPercent() +
            ", inactiv='" + getInactiv() + "'" +
            ", newVatId=" + getNewVatId() +
            "}";
    }
}
