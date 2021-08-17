package ch.united.fastadmin.domain;

import ch.united.fastadmin.domain.enumeration.CostUnitType;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CostUnit.
 */
@Entity
@Table(name = "cost_unit")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CostUnit implements Serializable {

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
     * cost unit's (Kostenstelle) internal number
     */
    @Column(name = "number")
    private String number;

    /**
     * cost unit's name
     */
    @Column(name = "name")
    private String name;

    /**
     * cost unit's description
     */
    @Column(name = "description")
    private String description;

    /**
     * cost unit's type: one of: ['P','U'] ,
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private CostUnitType type;

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

    public CostUnit id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getRemoteId() {
        return this.remoteId;
    }

    public CostUnit remoteId(Integer remoteId) {
        this.remoteId = remoteId;
        return this;
    }

    public void setRemoteId(Integer remoteId) {
        this.remoteId = remoteId;
    }

    public String getNumber() {
        return this.number;
    }

    public CostUnit number(String number) {
        this.number = number;
        return this;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return this.name;
    }

    public CostUnit name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public CostUnit description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CostUnitType getType() {
        return this.type;
    }

    public CostUnit type(CostUnitType type) {
        this.type = type;
        return this;
    }

    public void setType(CostUnitType type) {
        this.type = type;
    }

    public Boolean getInactiv() {
        return this.inactiv;
    }

    public CostUnit inactiv(Boolean inactiv) {
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
        if (!(o instanceof CostUnit)) {
            return false;
        }
        return id != null && id.equals(((CostUnit) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CostUnit{" +
            "id=" + getId() +
            ", remoteId=" + getRemoteId() +
            ", number='" + getNumber() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", type='" + getType() + "'" +
            ", inactiv='" + getInactiv() + "'" +
            "}";
    }
}
