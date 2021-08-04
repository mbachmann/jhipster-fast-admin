package ch.united.fastadmin.service.dto;

import ch.united.fastadmin.domain.enumeration.DomainResource;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link ch.united.fastadmin.domain.Permission} entity.
 */
@ApiModel(description = "actions that can be performed on an item (possible values: view, edit)")
public class PermissionDTO implements Serializable {

    private Long id;

    @NotNull
    private Boolean newAll;

    @NotNull
    private Boolean editOwn;

    @NotNull
    private Boolean editAll;

    @NotNull
    private Boolean viewOwn;

    @NotNull
    private Boolean viewAll;

    @NotNull
    private Boolean manageOwn;

    @NotNull
    private Boolean manageAll;

    @NotNull
    private DomainResource domainResource;

    private RoleDTO role;

    private ContactDTO contact;

    private ContactAddressDTO contactAddress;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getNewAll() {
        return newAll;
    }

    public void setNewAll(Boolean newAll) {
        this.newAll = newAll;
    }

    public Boolean getEditOwn() {
        return editOwn;
    }

    public void setEditOwn(Boolean editOwn) {
        this.editOwn = editOwn;
    }

    public Boolean getEditAll() {
        return editAll;
    }

    public void setEditAll(Boolean editAll) {
        this.editAll = editAll;
    }

    public Boolean getViewOwn() {
        return viewOwn;
    }

    public void setViewOwn(Boolean viewOwn) {
        this.viewOwn = viewOwn;
    }

    public Boolean getViewAll() {
        return viewAll;
    }

    public void setViewAll(Boolean viewAll) {
        this.viewAll = viewAll;
    }

    public Boolean getManageOwn() {
        return manageOwn;
    }

    public void setManageOwn(Boolean manageOwn) {
        this.manageOwn = manageOwn;
    }

    public Boolean getManageAll() {
        return manageAll;
    }

    public void setManageAll(Boolean manageAll) {
        this.manageAll = manageAll;
    }

    public DomainResource getDomainResource() {
        return domainResource;
    }

    public void setDomainResource(DomainResource domainResource) {
        this.domainResource = domainResource;
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

    public ContactAddressDTO getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(ContactAddressDTO contactAddress) {
        this.contactAddress = contactAddress;
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
            ", newAll='" + getNewAll() + "'" +
            ", editOwn='" + getEditOwn() + "'" +
            ", editAll='" + getEditAll() + "'" +
            ", viewOwn='" + getViewOwn() + "'" +
            ", viewAll='" + getViewAll() + "'" +
            ", manageOwn='" + getManageOwn() + "'" +
            ", manageAll='" + getManageAll() + "'" +
            ", domainResource='" + getDomainResource() + "'" +
            ", role=" + getRole() +
            ", contact=" + getContact() +
            ", contactAddress=" + getContactAddress() +
            "}";
    }
}
