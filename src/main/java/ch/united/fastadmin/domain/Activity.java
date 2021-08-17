package ch.united.fastadmin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Reporting Activity like consulting with an optional relation to a catalog service
 */
@Entity
@Table(name = "activity")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Activity implements Serializable {

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
     * name of activity eg. Consulting
     */
    @Column(name = "name")
    private String name;

    /**
     * use price defined in service catalog
     */
    @Column(name = "use_service_price")
    private Boolean useServicePrice;

    /**
     * is not active anymore
     */
    @Column(name = "inactiv")
    private Boolean inactiv;

    /**
     * The related activity to this effort
     */
    @ManyToOne
    @JsonIgnoreProperties(value = { "customFields", "category", "unit", "valueAddedTax" }, allowSetters = true)
    private CatalogService activity;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Activity id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getRemoteId() {
        return this.remoteId;
    }

    public Activity remoteId(Integer remoteId) {
        this.remoteId = remoteId;
        return this;
    }

    public void setRemoteId(Integer remoteId) {
        this.remoteId = remoteId;
    }

    public String getName() {
        return this.name;
    }

    public Activity name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getUseServicePrice() {
        return this.useServicePrice;
    }

    public Activity useServicePrice(Boolean useServicePrice) {
        this.useServicePrice = useServicePrice;
        return this;
    }

    public void setUseServicePrice(Boolean useServicePrice) {
        this.useServicePrice = useServicePrice;
    }

    public Boolean getInactiv() {
        return this.inactiv;
    }

    public Activity inactiv(Boolean inactiv) {
        this.inactiv = inactiv;
        return this;
    }

    public void setInactiv(Boolean inactiv) {
        this.inactiv = inactiv;
    }

    public CatalogService getActivity() {
        return this.activity;
    }

    public Activity activity(CatalogService catalogService) {
        this.setActivity(catalogService);
        return this;
    }

    public void setActivity(CatalogService catalogService) {
        this.activity = catalogService;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Activity)) {
            return false;
        }
        return id != null && id.equals(((Activity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Activity{" +
            "id=" + getId() +
            ", remoteId=" + getRemoteId() +
            ", name='" + getName() + "'" +
            ", useServicePrice='" + getUseServicePrice() + "'" +
            ", inactiv='" + getInactiv() + "'" +
            "}";
    }
}
