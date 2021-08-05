package ch.united.fastadmin.service.dto;

import ch.united.fastadmin.domain.enumeration.DomainArea;
import ch.united.fastadmin.domain.enumeration.PermissionType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ch.united.fastadmin.domain.Permission} entity.
 */
@ApiModel(description = "actions that can be performed on an item (possible values: view, edit)")
public class PermissionDTO implements Serializable {

    private Long id;

    /**
     * the use can add a new resource
     */
    @NotNull
    @ApiModelProperty(value = "the use can add a new resource", required = true)
    private PermissionType add;

    /**
     * the use can edit a resource
     */
    @NotNull
    @ApiModelProperty(value = "the use can edit a resource", required = true)
    private PermissionType edit;

    /**
     * the use can manage a resource
     */
    @NotNull
    @ApiModelProperty(value = "the use can manage a resource", required = true)
    private PermissionType manage;

    /**
     * the permission is for the domain resource
     */
    @NotNull
    @ApiModelProperty(value = "the permission is for the domain resource", required = true)
    private DomainArea domainArea;

    private RoleDTO role;

    private ContactDTO contact;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PermissionType getAdd() {
        return add;
    }

    public void setAdd(PermissionType add) {
        this.add = add;
    }

    public PermissionType getEdit() {
        return edit;
    }

    public void setEdit(PermissionType edit) {
        this.edit = edit;
    }

    public PermissionType getManage() {
        return manage;
    }

    public void setManage(PermissionType manage) {
        this.manage = manage;
    }

    public DomainArea getDomainArea() {
        return domainArea;
    }

    public void setDomainArea(DomainArea domainArea) {
        this.domainArea = domainArea;
    }

    public RoleDTO getRole() {
        return role;
    }

    public void setRole(RoleDTO role) {
        this.role = role;
    }

    public ContactDTO getContact() {
        return contact;
    }

    public void setContact(ContactDTO contact) {
        this.contact = contact;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PermissionDTO)) {
            return false;
        }

        PermissionDTO permissionDTO = (PermissionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, permissionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PermissionDTO{" +
            "id=" + getId() +
            ", add='" + getAdd() + "'" +
            ", edit='" + getEdit() + "'" +
            ", manage='" + getManage() + "'" +
            ", domainArea='" + getDomainArea() + "'" +
            ", role=" + getRole() +
            ", contact=" + getContact() +
            "}";
    }
}
