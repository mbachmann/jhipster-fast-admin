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
@Table(name = "reporting_activity")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ReportingActivity implements Serializable {

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
    private CatalogService catalogService;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReportingActivity id(Long id) {
        this.id = id;
        return this;
    }

    public Integer getRemoteId() {
        return this.remoteId;
    }

    public ReportingActivity remoteId(Integer remoteId) {
        this.remoteId = remoteId;
        return this;
    }

    public void setRemoteId(Integer remoteId) {
        this.remoteId = remoteId;
    }

    public String getName() {
        return this.name;
    }

    public ReportingActivity name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getUseServicePrice() {
        return this.useServicePrice;
    }

    public ReportingActivity useServicePrice(Boolean useServicePrice) {
        this.useServicePrice = useServicePrice;
        return this;
    }

    public void setUseServicePrice(Boolean useServicePrice) {
        this.useServicePrice = useServicePrice;
    }

    public Boolean getInactiv() {
        return this.inactiv;
    }

    public ReportingActivity inactiv(Boolean inactiv) {
        this.inactiv = inactiv;
        return this;
    }

    public void setInactiv(Boolean inactiv) {
        this.inactiv = inactiv;
    }

    public CatalogService getCatalogService() {
        return this.catalogService;
    }

    public ReportingActivity catalogService(CatalogService catalogService) {
        this.setCatalogService(catalogService);
        return this;
    }

    public void setCatalogService(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportingActivity)) {
            return false;
        }
        return id != null && id.equals(((ReportingActivity) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportingActivity{" +
            "id=" + getId() +
            ", remoteId=" + getRemoteId() +
            ", name='" + getName() + "'" +
            ", useServicePrice='" + getUseServicePrice() + "'" +
            ", inactiv='" + getInactiv() + "'" +
            "}";
    }
}
