package ch.united.fastadmin.domain;

import ch.united.fastadmin.domain.enumeration.VatType;
import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Value added tax
 */
@Entity
@Table(name = "vat")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Vat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the VAT
     */
    @Column(name = "name")
    private String name;

    /**
     * VAT Type percent, no_vat, free_input
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "vat_type")
    private VatType vatType;

    /**
     * VAT is valid from
     */
    @Column(name = "valid_from")
    private LocalDate validFrom;

    /**
     * VAT is valid until
     */
    @Column(name = "valid_until")
    private LocalDate validUntil;

    /**
     * The VAT percent
     */
    @Column(name = "vat_percent")
    private Double vatPercent;

    /**
     * is not active anymore
     */
    @Column(name = "inactiv")
    private Boolean inactiv;

    /**
     * replaced by
     */
    @Column(name = "new_vat_id")
    private Integer newVatId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vat id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Vat name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public VatType getVatType() {
        return this.vatType;
    }

    public Vat vatType(VatType vatType) {
        this.vatType = vatType;
        return this;
    }

    public void setVatType(VatType vatType) {
        this.vatType = vatType;
    }

    public LocalDate getValidFrom() {
        return this.validFrom;
    }

    public Vat validFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
        return this;
    }

    public void setValidFrom(LocalDate validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDate getValidUntil() {
        return this.validUntil;
    }

    public Vat validUntil(LocalDate validUntil) {
        this.validUntil = validUntil;
        return this;
    }

    public void setValidUntil(LocalDate validUntil) {
        this.validUntil = validUntil;
    }

    public Double getVatPercent() {
        return this.vatPercent;
    }

    public Vat vatPercent(Double vatPercent) {
        this.vatPercent = vatPercent;
        return this;
    }

    public void setVatPercent(Double vatPercent) {
        this.vatPercent = vatPercent;
    }

    public Boolean getInactiv() {
        return this.inactiv;
    }

    public Vat inactiv(Boolean inactiv) {
        this.inactiv = inactiv;
        return this;
    }

    public void setInactiv(Boolean inactiv) {
        this.inactiv = inactiv;
    }

    public Integer getNewVatId() {
        return this.newVatId;
    }

    public Vat newVatId(Integer newVatId) {
        this.newVatId = newVatId;
        return this;
    }

    public void setNewVatId(Integer newVatId) {
        this.newVatId = newVatId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Vat)) {
            return false;
        }
        return id != null && id.equals(((Vat) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Vat{" +
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
