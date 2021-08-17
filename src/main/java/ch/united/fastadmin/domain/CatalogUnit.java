package ch.united.fastadmin.domain;

import ch.united.fastadmin.domain.enumeration.CatalogScope;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * System Units\n\n14  -         All\n17  Credit    All\n13  Flat      All\n7  Item      Product\n9  Litres    Product\n16  Metres    Product\n15  Minutes   All\n8  kg        Product\n12  km        All\n10  m2        Product\n11  m3        Product
 */
@Entity
@Table(name = "catalog_unit")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CatalogUnit implements Serializable {

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
     * Name of the Unit
     */
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "name_de")
    private String nameDe;

    @Column(name = "name_en")
    private String nameEn;

    @Column(name = "name_fr")
    private String nameFr;

    @Column(name = "name_it")
    private String nameIt;

    /**
     * scope, one of: s - service, p - product, a - all
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "scope")
    private CatalogScope scope;

    /**
     * is unit a custom company unit (if not, it's a system unit)
     */
    @Column(name = "custom")
    private Boolean custom;

    /**
     * is not active anymore
     */
    @Column(name = "inactiv")
    private Boolean inactiv;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CatalogUnit id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getRemoteId() {
        return this.remoteId;
    }

    public CatalogUnit remoteId(Integer remoteId) {
        this.remoteId = remoteId;
        return this;
    }

    public void setRemoteId(Integer remoteId) {
        this.remoteId = remoteId;
    }

    public String getName() {
        return this.name;
    }

    public CatalogUnit name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameDe() {
        return this.nameDe;
    }

    public CatalogUnit nameDe(String nameDe) {
        this.nameDe = nameDe;
        return this;
    }

    public void setNameDe(String nameDe) {
        this.nameDe = nameDe;
    }

    public String getNameEn() {
        return this.nameEn;
    }

    public CatalogUnit nameEn(String nameEn) {
        this.nameEn = nameEn;
        return this;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameFr() {
        return this.nameFr;
    }

    public CatalogUnit nameFr(String nameFr) {
        this.nameFr = nameFr;
        return this;
    }

    public void setNameFr(String nameFr) {
        this.nameFr = nameFr;
    }

    public String getNameIt() {
        return this.nameIt;
    }

    public CatalogUnit nameIt(String nameIt) {
        this.nameIt = nameIt;
        return this;
    }

    public void setNameIt(String nameIt) {
        this.nameIt = nameIt;
    }

    public CatalogScope getScope() {
        return this.scope;
    }

    public CatalogUnit scope(CatalogScope scope) {
        this.scope = scope;
        return this;
    }

    public void setScope(CatalogScope scope) {
        this.scope = scope;
    }

    public Boolean getCustom() {
        return this.custom;
    }

    public CatalogUnit custom(Boolean custom) {
        this.custom = custom;
        return this;
    }

    public void setCustom(Boolean custom) {
        this.custom = custom;
    }

    public Boolean getInactiv() {
        return this.inactiv;
    }

    public CatalogUnit inactiv(Boolean inactiv) {
        this.inactiv = inactiv;
        return this;
    }

    public void setInactiv(Boolean inactiv) {
        this.inactiv = inactiv;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CatalogUnit)) {
            return false;
        }
        return id != null && id.equals(((CatalogUnit) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CatalogUnit{" +
            "id=" + getId() +
            ", remoteId=" + getRemoteId() +
            ", name='" + getName() + "'" +
            ", nameDe='" + getNameDe() + "'" +
            ", nameEn='" + getNameEn() + "'" +
            ", nameFr='" + getNameFr() + "'" +
            ", nameIt='" + getNameIt() + "'" +
            ", scope='" + getScope() + "'" +
            ", custom='" + getCustom() + "'" +
            ", inactiv='" + getInactiv() + "'" +
            "}";
    }
}
