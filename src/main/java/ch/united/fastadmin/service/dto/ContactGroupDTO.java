package ch.united.fastadmin.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ch.united.fastadmin.domain.ContactGroup} entity.
 */
@ApiModel(description = "The group a contact belongs to")
public class ContactGroupDTO implements Serializable {

    private Long id;

    /**
     * id of a remote system
     */

    @ApiModelProperty(value = "id of a remote system")
    private Integer remoteId;

    /**
     * Name of the contact group
     */
    @NotNull
    @ApiModelProperty(value = "Name of the contact group", required = true)
    private String name;

    /**
     * how many contacts are assigned to this group ,
     */
    @ApiModelProperty(value = "how many contacts are assigned to this group ,")
    private Integer usage;

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

    public Integer getUsage() {
        return usage;
    }

    public void setUsage(Integer usage) {
        this.usage = usage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContactGroupDTO)) {
            return false;
        }

        ContactGroupDTO contactGroupDTO = (ContactGroupDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, contactGroupDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ContactGroupDTO{" +
            "id=" + getId() +
            ", remoteId=" + getRemoteId() +
            ", name='" + getName() + "'" +
            ", usage=" + getUsage() +
            "}";
    }
}
