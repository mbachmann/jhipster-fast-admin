package ch.united.fastadmin.service.dto;

import ch.united.fastadmin.domain.enumeration.CostUnitType;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ch.united.fastadmin.domain.CostUnit} entity.
 */
public class CostUnitDTO implements Serializable {

    private Long id;

    /**
     * id of a remote system
     */

    @ApiModelProperty(value = "id of a remote system")
    private Integer remoteId;

    /**
     * cost unit's (Kostenstelle) internal number
     */
    @ApiModelProperty(value = "cost unit's (Kostenstelle) internal number")
    private String number;

    /**
     * cost unit's name
     */
    @ApiModelProperty(value = "cost unit's name")
    private String name;

    /**
     * cost unit's description
     */
    @ApiModelProperty(value = "cost unit's description")
    private String description;

    /**
     * cost unit's type: one of: ['P','U'] ,
     */
    @ApiModelProperty(value = "cost unit's type: one of: ['P','U'] ,")
    private CostUnitType type;

    /**
     * is not active anymore
     */
    @ApiModelProperty(value = "is not active anymore")
    private Boolean inactiv;

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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CostUnitType getType() {
        return type;
    }

    public void setType(CostUnitType type) {
        this.type = type;
    }

    public Boolean getInactiv() {
        return inactiv;
    }

    public void setInactiv(Boolean inactiv) {
        this.inactiv = inactiv;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CostUnitDTO)) {
            return false;
        }

        CostUnitDTO costUnitDTO = (CostUnitDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, costUnitDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CostUnitDTO{" +
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
