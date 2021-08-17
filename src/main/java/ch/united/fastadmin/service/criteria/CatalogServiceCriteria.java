package ch.united.fastadmin.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link ch.united.fastadmin.domain.CatalogService} entity. This class is used
 * in {@link ch.united.fastadmin.web.rest.CatalogServiceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /catalog-services?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CatalogServiceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter remoteId;

    private StringFilter number;

    private StringFilter name;

    private StringFilter description;

    private StringFilter notes;

    private StringFilter categoryName;

    private BooleanFilter includingVat;

    private DoubleFilter vat;

    private StringFilter unitName;

    private DoubleFilter price;

    private IntegerFilter defaultAmount;

    private ZonedDateTimeFilter created;

    private BooleanFilter inactiv;

    private LongFilter customFieldsId;

    private LongFilter categoryId;

    private LongFilter unitId;

    private LongFilter valueAddedTaxId;

    public CatalogServiceCriteria() {}

    public CatalogServiceCriteria(CatalogServiceCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.remoteId = other.remoteId == null ? null : other.remoteId.copy();
        this.number = other.number == null ? null : other.number.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.notes = other.notes == null ? null : other.notes.copy();
        this.categoryName = other.categoryName == null ? null : other.categoryName.copy();
        this.includingVat = other.includingVat == null ? null : other.includingVat.copy();
        this.vat = other.vat == null ? null : other.vat.copy();
        this.unitName = other.unitName == null ? null : other.unitName.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.defaultAmount = other.defaultAmount == null ? null : other.defaultAmount.copy();
        this.created = other.created == null ? null : other.created.copy();
        this.inactiv = other.inactiv == null ? null : other.inactiv.copy();
        this.customFieldsId = other.customFieldsId == null ? null : other.customFieldsId.copy();
        this.categoryId = other.categoryId == null ? null : other.categoryId.copy();
        this.unitId = other.unitId == null ? null : other.unitId.copy();
        this.valueAddedTaxId = other.valueAddedTaxId == null ? null : other.valueAddedTaxId.copy();
    }

    @Override
    public CatalogServiceCriteria copy() {
        return new CatalogServiceCriteria(this);
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

    public IntegerFilter getRemoteId() {
        return remoteId;
    }

    public IntegerFilter remoteId() {
        if (remoteId == null) {
            remoteId = new IntegerFilter();
        }
        return remoteId;
    }

    public void setRemoteId(IntegerFilter remoteId) {
        this.remoteId = remoteId;
    }

    public StringFilter getNumber() {
        return number;
    }

    public StringFilter number() {
        if (number == null) {
            number = new StringFilter();
        }
        return number;
    }

    public void setNumber(StringFilter number) {
        this.number = number;
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

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getNotes() {
        return notes;
    }

    public StringFilter notes() {
        if (notes == null) {
            notes = new StringFilter();
        }
        return notes;
    }

    public void setNotes(StringFilter notes) {
        this.notes = notes;
    }

    public StringFilter getCategoryName() {
        return categoryName;
    }

    public StringFilter categoryName() {
        if (categoryName == null) {
            categoryName = new StringFilter();
        }
        return categoryName;
    }

    public void setCategoryName(StringFilter categoryName) {
        this.categoryName = categoryName;
    }

    public BooleanFilter getIncludingVat() {
        return includingVat;
    }

    public BooleanFilter includingVat() {
        if (includingVat == null) {
            includingVat = new BooleanFilter();
        }
        return includingVat;
    }

    public void setIncludingVat(BooleanFilter includingVat) {
        this.includingVat = includingVat;
    }

    public DoubleFilter getVat() {
        return vat;
    }

    public DoubleFilter vat() {
        if (vat == null) {
            vat = new DoubleFilter();
        }
        return vat;
    }

    public void setVat(DoubleFilter vat) {
        this.vat = vat;
    }

    public StringFilter getUnitName() {
        return unitName;
    }

    public StringFilter unitName() {
        if (unitName == null) {
            unitName = new StringFilter();
        }
        return unitName;
    }

    public void setUnitName(StringFilter unitName) {
        this.unitName = unitName;
    }

    public DoubleFilter getPrice() {
        return price;
    }

    public DoubleFilter price() {
        if (price == null) {
            price = new DoubleFilter();
        }
        return price;
    }

    public void setPrice(DoubleFilter price) {
        this.price = price;
    }

    public IntegerFilter getDefaultAmount() {
        return defaultAmount;
    }

    public IntegerFilter defaultAmount() {
        if (defaultAmount == null) {
            defaultAmount = new IntegerFilter();
        }
        return defaultAmount;
    }

    public void setDefaultAmount(IntegerFilter defaultAmount) {
        this.defaultAmount = defaultAmount;
    }

    public ZonedDateTimeFilter getCreated() {
        return created;
    }

    public ZonedDateTimeFilter created() {
        if (created == null) {
            created = new ZonedDateTimeFilter();
        }
        return created;
    }

    public void setCreated(ZonedDateTimeFilter created) {
        this.created = created;
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

    public LongFilter getCustomFieldsId() {
        return customFieldsId;
    }

    public LongFilter customFieldsId() {
        if (customFieldsId == null) {
            customFieldsId = new LongFilter();
        }
        return customFieldsId;
    }

    public void setCustomFieldsId(LongFilter customFieldsId) {
        this.customFieldsId = customFieldsId;
    }

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public LongFilter categoryId() {
        if (categoryId == null) {
            categoryId = new LongFilter();
        }
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
    }

    public LongFilter getUnitId() {
        return unitId;
    }

    public LongFilter unitId() {
        if (unitId == null) {
            unitId = new LongFilter();
        }
        return unitId;
    }

    public void setUnitId(LongFilter unitId) {
        this.unitId = unitId;
    }

    public LongFilter getValueAddedTaxId() {
        return valueAddedTaxId;
    }

    public LongFilter valueAddedTaxId() {
        if (valueAddedTaxId == null) {
            valueAddedTaxId = new LongFilter();
        }
        return valueAddedTaxId;
    }

    public void setValueAddedTaxId(LongFilter valueAddedTaxId) {
        this.valueAddedTaxId = valueAddedTaxId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CatalogServiceCriteria that = (CatalogServiceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(remoteId, that.remoteId) &&
            Objects.equals(number, that.number) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(notes, that.notes) &&
            Objects.equals(categoryName, that.categoryName) &&
            Objects.equals(includingVat, that.includingVat) &&
            Objects.equals(vat, that.vat) &&
            Objects.equals(unitName, that.unitName) &&
            Objects.equals(price, that.price) &&
            Objects.equals(defaultAmount, that.defaultAmount) &&
            Objects.equals(created, that.created) &&
            Objects.equals(inactiv, that.inactiv) &&
            Objects.equals(customFieldsId, that.customFieldsId) &&
            Objects.equals(categoryId, that.categoryId) &&
            Objects.equals(unitId, that.unitId) &&
            Objects.equals(valueAddedTaxId, that.valueAddedTaxId)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            remoteId,
            number,
            name,
            description,
            notes,
            categoryName,
            includingVat,
            vat,
            unitName,
            price,
            defaultAmount,
            created,
            inactiv,
            customFieldsId,
            categoryId,
            unitId,
            valueAddedTaxId
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CatalogServiceCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (remoteId != null ? "remoteId=" + remoteId + ", " : "") +
            (number != null ? "number=" + number + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (notes != null ? "notes=" + notes + ", " : "") +
            (categoryName != null ? "categoryName=" + categoryName + ", " : "") +
            (includingVat != null ? "includingVat=" + includingVat + ", " : "") +
            (vat != null ? "vat=" + vat + ", " : "") +
            (unitName != null ? "unitName=" + unitName + ", " : "") +
            (price != null ? "price=" + price + ", " : "") +
            (defaultAmount != null ? "defaultAmount=" + defaultAmount + ", " : "") +
            (created != null ? "created=" + created + ", " : "") +
            (inactiv != null ? "inactiv=" + inactiv + ", " : "") +
            (customFieldsId != null ? "customFieldsId=" + customFieldsId + ", " : "") +
            (categoryId != null ? "categoryId=" + categoryId + ", " : "") +
            (unitId != null ? "unitId=" + unitId + ", " : "") +
            (valueAddedTaxId != null ? "valueAddedTaxId=" + valueAddedTaxId + ", " : "") +
            "}";
    }
}
