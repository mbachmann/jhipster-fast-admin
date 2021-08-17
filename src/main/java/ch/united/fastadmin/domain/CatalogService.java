package ch.united.fastadmin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CatalogService.
 */
@Entity
@Table(name = "catalog_service")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CatalogService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * id of a remote system
     */

    @Column(name = "remote_id", unique = true)
    private Integer remoteId;

    /**
     * Catalog Product Number
     */

    @Column(name = "number", unique = true)
    private String number;

    /**
     * Catalog Product Name
     */
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * Catalog Product Description
     */
    @Column(name = "description")
    private String description;

    /**
     * Catalog Product Notes
     */
    @Column(name = "notes")
    private String notes;

    /**
     * Category Name
     */
    @Column(name = "category_name")
    private String categoryName;

    /**
     * The price includes MWST
     */
    @Column(name = "including_vat")
    private Boolean includingVat;

    /**
     * The VAT Percent
     */
    @Column(name = "vat")
    private Double vat;

    /**
     * The unit Name
     */
    @Column(name = "unit_name")
    private String unitName;

    /**
     * The sales price of the product
     */
    @Column(name = "price")
    private Double price;

    /**
     * Default amount of products (Standard Menge)
     */
    @Column(name = "default_amount")
    private Integer defaultAmount;

    @Column(name = "created")
    private ZonedDateTime created;

    /**
     * is not active anymore
     */
    @Column(name = "inactiv")
    private Boolean inactiv;

    /**
     * custom edit fields
     */
    @OneToMany(mappedBy = "catalogService")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "customField", "contact", "contactPerson", "project", "catalogProduct", "catalogService", "documentLetter", "deliveryNote",
        },
        allowSetters = true
    )
    private Set<CustomFieldValue> customFields = new HashSet<>();

    /**
     * The category relation
     */
    @ManyToOne
    private CatalogCategory category;

    /**
     * The unit relation
     */
    @ManyToOne
    private CatalogUnit unit;

    /**
     * The vat relation
     */
    @ManyToOne
    private Vat vat;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CatalogService id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getRemoteId() {
        return this.remoteId;
    }

    public CatalogService remoteId(Integer remoteId) {
        this.remoteId = remoteId;
        return this;
    }

    public void setRemoteId(Integer remoteId) {
        this.remoteId = remoteId;
    }

    public String getNumber() {
        return this.number;
    }

    public CatalogService number(String number) {
        this.number = number;
        return this;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return this.name;
    }

    public CatalogService name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public CatalogService description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotes() {
        return this.notes;
    }

    public CatalogService notes(String notes) {
        this.notes = notes;
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public CatalogService categoryName(String categoryName) {
        this.categoryName = categoryName;
        return this;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Boolean getIncludingVat() {
        return this.includingVat;
    }

    public CatalogService includingVat(Boolean includingVat) {
        this.includingVat = includingVat;
        return this;
    }

    public void setIncludingVat(Boolean includingVat) {
        this.includingVat = includingVat;
    }

    public Double getVat() {
        return this.vat;
    }

    public CatalogService vat(Double vat) {
        this.vat = vat;
        return this;
    }

    public void setVat(Double vat) {
        this.vat = vat;
    }

    public String getUnitName() {
        return this.unitName;
    }

    public CatalogService unitName(String unitName) {
        this.unitName = unitName;
        return this;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Double getPrice() {
        return this.price;
    }

    public CatalogService price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getDefaultAmount() {
        return this.defaultAmount;
    }

    public CatalogService defaultAmount(Integer defaultAmount) {
        this.defaultAmount = defaultAmount;
        return this;
    }

    public void setDefaultAmount(Integer defaultAmount) {
        this.defaultAmount = defaultAmount;
    }

    public ZonedDateTime getCreated() {
        return this.created;
    }

    public CatalogService created(ZonedDateTime created) {
        this.created = created;
        return this;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public Boolean getInactiv() {
        return this.inactiv;
    }

    public CatalogService inactiv(Boolean inactiv) {
        this.inactiv = inactiv;
        return this;
    }

    public void setInactiv(Boolean inactiv) {
        this.inactiv = inactiv;
    }

    public Set<CustomFieldValue> getCustomFields() {
        return this.customFields;
    }

    public CatalogService customFields(Set<CustomFieldValue> customFieldValues) {
        this.setCustomFields(customFieldValues);
        return this;
    }

    public CatalogService addCustomFields(CustomFieldValue customFieldValue) {
        this.customFields.add(customFieldValue);
        customFieldValue.setCatalogService(this);
        return this;
    }

    public CatalogService removeCustomFields(CustomFieldValue customFieldValue) {
        this.customFields.remove(customFieldValue);
        customFieldValue.setCatalogService(null);
        return this;
    }

    public void setCustomFields(Set<CustomFieldValue> customFieldValues) {
        if (this.customFields != null) {
            this.customFields.forEach(i -> i.setCatalogService(null));
        }
        if (customFieldValues != null) {
            customFieldValues.forEach(i -> i.setCatalogService(this));
        }
        this.customFields = customFieldValues;
    }

    public CatalogCategory getCategory() {
        return this.category;
    }

    public CatalogService category(CatalogCategory catalogCategory) {
        this.setCategory(catalogCategory);
        return this;
    }

    public void setCategory(CatalogCategory catalogCategory) {
        this.category = catalogCategory;
    }

    public CatalogUnit getUnit() {
        return this.unit;
    }

    public CatalogService unit(CatalogUnit catalogUnit) {
        this.setUnit(catalogUnit);
        return this;
    }

    public void setUnit(CatalogUnit catalogUnit) {
        this.unit = catalogUnit;
    }

    public Vat getVat() {
        return this.vat;
    }

    public CatalogService vat(Vat vat) {
        this.setVat(vat);
        return this;
    }

    public void setVat(Vat vat) {
        this.vat = vat;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CatalogService)) {
            return false;
        }
        return id != null && id.equals(((CatalogService) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CatalogService{" +
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
            "}";
    }
}
