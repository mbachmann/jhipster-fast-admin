package ch.united.fastadmin.service.criteria;

import ch.united.fastadmin.domain.enumeration.VatType;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link ch.united.fastadmin.domain.ValueAddedTax} entity. This class is used
 * in {@link ch.united.fastadmin.web.rest.ValueAddedTaxResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /value-added-taxes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ValueAddedTaxCriteria implements Serializable, Criteria {

    /**
     * Class for filtering VatType
     */
    public static class VatTypeFilter extends Filter<VatType> {

        public VatTypeFilter() {}

        public VatTypeFilter(VatTypeFilter filter) {
            super(filter);
        }

        @Override
        public VatTypeFilter copy() {
            return new VatTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private VatTypeFilter vatType;

    private LocalDateFilter validFrom;

    private LocalDateFilter validUntil;

    private DoubleFilter vatPercent;

    private BooleanFilter inactiv;

    private IntegerFilter newVatId;

    public ValueAddedTaxCriteria() {}

    public ValueAddedTaxCriteria(ValueAddedTaxCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.vatType = other.vatType == null ? null : other.vatType.copy();
        this.validFrom = other.validFrom == null ? null : other.validFrom.copy();
        this.validUntil = other.validUntil == null ? null : other.validUntil.copy();
        this.vatPercent = other.vatPercent == null ? null : other.vatPercent.copy();
        this.inactiv = other.inactiv == null ? null : other.inactiv.copy();
        this.newVatId = other.newVatId == null ? null : other.newVatId.copy();
    }

    @Override
    public ValueAddedTaxCriteria copy() {
        return new ValueAddedTaxCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public VatTypeFilter getVatType() {
        return vatType;
    }

    public VatTypeFilter vatType() {
        if (vatType == null) {
            vatType = new VatTypeFilter();
        }
        return vatType;
    }

    public void setVatType(VatTypeFilter vatType) {
        this.vatType = vatType;
    }

    public LocalDateFilter getValidFrom() {
        return validFrom;
    }

    public LocalDateFilter validFrom() {
        if (validFrom == null) {
            validFrom = new LocalDateFilter();
        }
        return validFrom;
    }

    public void setValidFrom(LocalDateFilter validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDateFilter getValidUntil() {
        return validUntil;
    }

    public LocalDateFilter validUntil() {
        if (validUntil == null) {
            validUntil = new LocalDateFilter();
        }
        return validUntil;
    }

    public void setValidUntil(LocalDateFilter validUntil) {
        this.validUntil = validUntil;
    }

    public DoubleFilter getVatPercent() {
        return vatPercent;
    }

    public DoubleFilter vatPercent() {
        if (vatPercent == null) {
            vatPercent = new DoubleFilter();
        }
        return vatPercent;
    }

    public void setVatPercent(DoubleFilter vatPercent) {
        this.vatPercent = vatPercent;
    }

    public BooleanFilter getInactiv() {
        return inactiv;
    }

    public BooleanFilter inactiv() {
        if (inactiv == null) {
            inactiv = new BooleanFilter();
        }
        return inactiv;
    }

    public void setInactiv(BooleanFilter inactiv) {
        this.inactiv = inactiv;
    }

    public IntegerFilter getNewVatId() {
        return newVatId;
    }

    public IntegerFilter newVatId() {
        if (newVatId == null) {
            newVatId = new IntegerFilter();
        }
        return newVatId;
    }

    public void setNewVatId(IntegerFilter newVatId) {
        this.newVatId = newVatId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ValueAddedTaxCriteria that = (ValueAddedTaxCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(vatType, that.vatType) &&
            Objects.equals(validFrom, that.validFrom) &&
            Objects.equals(validUntil, that.validUntil) &&
            Objects.equals(vatPercent, that.vatPercent) &&
            Objects.equals(inactiv, that.inactiv) &&
            Objects.equals(newVatId, that.newVatId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, vatType, validFrom, validUntil, vatPercent, inactiv, newVatId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ValueAddedTaxCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (vatType != null ? "vatType=" + vatType + ", " : "") +
            (validFrom != null ? "validFrom=" + validFrom + ", " : "") +
            (validUntil != null ? "validUntil=" + validUntil + ", " : "") +
            (vatPercent != null ? "vatPercent=" + vatPercent + ", " : "") +
            (inactiv != null ? "inactiv=" + inactiv + ", " : "") +
            (newVatId != null ? "newVatId=" + newVatId + ", " : "") +
            "}";
    }
}
