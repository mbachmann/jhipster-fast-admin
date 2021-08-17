package ch.united.fastadmin.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ch.united.fastadmin.domain.Activity} entity.
 */
@ApiModel(description = "Reporting Activity like consulting with an optional relation to a catalog service")
public class ActivityDTO implements Serializable {

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

    private CatalogServiceDTO activity;

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

    public CatalogServiceDTO getActivity() {
        return activity;
    }

    public void setActivity(CatalogServiceDTO activity) {
        this.activity = activity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ActivityDTO)) {
            return false;
        }

        ActivityDTO activityDTO = (ActivityDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, activityDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ActivityDTO{" +
            "id=" + getId() +
            ", remoteId=" + getRemoteId() +
            ", name='" + getName() + "'" +
            ", useServicePrice='" + getUseServicePrice() + "'" +
            ", inactiv='" + getInactiv() + "'" +
            ", activity=" + getActivity() +
            "}";
    }
}
