package ch.united.fastadmin.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ch.united.fastadmin.domain.ReportingActivity} entity.
 */
@ApiModel(description = "Reporting Activity like consulting with an optional relation to a catalog service")
public class ReportingActivityDTO implements Serializable {

    private Long id;

    /**
     * id of a remote system
     */

    @ApiModelProperty(value = "id of a remote system")
    private Integer remoteId;

    /**
     * name of activity eg. Consulting
     */
    @ApiModelProperty(value = "name of activity eg. Consulting")
    private String name;

    /**
     * use price defined in service catalog
     */
    @ApiModelProperty(value = "use price defined in service catalog")
    private Boolean useServicePrice;

    /**
     * is not active anymore
     */
    @ApiModelProperty(value = "is not active anymore")
    private Boolean inactiv;

    private CatalogServiceDTO catalogService;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getUseServicePrice() {
        return useServicePrice;
    }

    public void setUseServicePrice(Boolean useServicePrice) {
        this.useServicePrice = useServicePrice;
    }

    public Boolean getInactiv() {
        return inactiv;
    }

    public void setInactiv(Boolean inactiv) {
        this.inactiv = inactiv;
    }

    public CatalogServiceDTO getCatalogService() {
        return catalogService;
    }

    public void setCatalogService(CatalogServiceDTO catalogService) {
        this.catalogService = catalogService;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportingActivityDTO)) {
            return false;
        }

        ReportingActivityDTO reportingActivityDTO = (ReportingActivityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, reportingActivityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportingActivityDTO{" +
            "id=" + getId() +
            ", remoteId=" + getRemoteId() +
            ", name='" + getName() + "'" +
            ", useServicePrice='" + getUseServicePrice() + "'" +
            ", inactiv='" + getInactiv() + "'" +
            ", catalogService=" + getCatalogService() +
            "}";
    }
}
